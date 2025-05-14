package com.dairyProducts.details.service;

import com.dairyProducts.details.dto.ProductDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import com.dairyProducts.details.entity.ProductStock;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.ProductRepository;
import com.dairyProducts.details.repository.ProductStockRepository;
import com.dairyProducts.details.utility.CSVGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private ProductStockRepository productStockRepo;

    @Mock
    private CSVGenerator csvGenerator;

    @Test
    void testGetProductDetailsService_Success() {
        // Setup customer
        Customer customer = new Customer();
        customer.setCardNumber(123456L);
        customer.setCustomerName("John");
        customer.setPendingAmount(0.0);
        customer.setDefaulter("NO");

        // Setup product
        Product product = new Product();
        product.setPaid("NO");
        product.setQuantity(2);
        product.setUnitPrice(10.0);
        product.setProductName("Milk");
        product.setCustomer(customer);

        // Mock repositories and CSV generator
        when(productRepo.findByCustomerCardNumber(123456L)).thenReturn(List.of(product)); // Ensure product list is returned
        when(customerRepo.findByCardNumber(123456L)).thenReturn(Optional.of(customer));
        when(csvGenerator.generateCSV(eq(customer), eq(List.of(product)))).thenReturn("dummy.csv");

        // Call method under test
        ResponseEntity<?> response = productService.getProductDetailsService(123456L);

        // Verify that generateCSV was called
        verify(csvGenerator, times(1)).generateCSV(eq(customer), eq(List.of(product)));

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("John"));
        assertTrue(response.getBody().toString().contains("dummy.csv"));
    }

    @Test
    void testGetProductDetailsService_NoProducts() {
        when(productRepo.findByCustomerCardNumber(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = productService.getProductDetailsService(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("No details found"));
    }

    @Test
    void testGetProductDetailsService_CustomerNotFound() {
        long cardNumber = 123456L;
        Product product = new Product();
        product.setPaid("NO");
        product.setQuantity(1);
        product.setUnitPrice(5.0);

        when(productRepo.findByCustomerCardNumber(cardNumber)).thenReturn(List.of(product));
        when(customerRepo.findByCardNumber(cardNumber)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            productService.getProductDetailsService(cardNumber);
        });

        assertEquals("Customer not found for cardNumber: " + cardNumber, thrown.getMessage());
    }
    @Test
    void testAddProductDetailsService_Success() {
        long cardNumber = 111L;
        Customer customer = new Customer();
        customer.setCardNumber(cardNumber);
        customer.setPendingAmount(0.0);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setQuantity(2);
        productDTO.setProductName("MILK");

        ProductStock stock = new ProductStock();
        stock.setBalanceQuantity(10);

        when(customerRepo.findByCardNumber(cardNumber)).thenReturn(Optional.of(customer));
        when(productStockRepo.findTopByOrderByLoadedDateDesc()).thenReturn(stock);
        when(productRepo.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId("123");
            return p;
        });

        ResponseEntity<String> response = productService.addProductDetailsService(cardNumber, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("successfully added"));
    }
    @Test
    void testAddProductDetailsService_MissingFields() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("");
        dto.setQuantity(0);

        when(customerRepo.findByCardNumber(123L)).thenReturn(Optional.of(new Customer()));
        ResponseEntity<String> response = productService.addProductDetailsService(123L, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("mandatory"));
    }
    @Test
    void testAddProductDetailsService_InvalidProductName() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("INVALID");
        dto.setQuantity(1);

        Customer customer = new Customer();
        customer.setCardNumber(123L);

        when(customerRepo.findByCardNumber(123L)).thenReturn(Optional.of(customer));

        ResponseEntity<String> response = productService.addProductDetailsService(123L, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid product name"));
    }
    @Test
    void testAddProductDetailsService_InsufficientStock() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("MILK");
        dto.setQuantity(100);

        Customer customer = new Customer();
        customer.setCardNumber(123L);

        ProductStock stock = new ProductStock();
        stock.setBalanceQuantity(10);  // Not enough

        when(customerRepo.findByCardNumber(123L)).thenReturn(Optional.of(customer));
        when(productStockRepo.findTopByOrderByLoadedDateDesc()).thenReturn(stock);

        ResponseEntity<String> response = productService.addProductDetailsService(123L, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Product stock is not available"));
    }
    @Test
    void testUpdateProductDetailsService_Success() {
        ProductDTO dto = new ProductDTO();
        dto.setId("p1");
        dto.setQuantity(3);
        dto.setProductName("MILK");
        dto.setPaid("N");

        Customer customer = new Customer();
        customer.setCardNumber(111L);
        customer.setPendingAmount(10);

        Product existingProduct = new Product();
        existingProduct.setId("p1");
        existingProduct.setCustomer(customer);
        existingProduct.setTotalPrice(5);

        when(productRepo.findById("p1")).thenReturn(Optional.of(existingProduct));
        when(customerRepo.findByCardNumber(111L)).thenReturn(Optional.of(customer));

        ResponseEntity<String> response = productService.updateProductDetailsService(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateProductDetailsService_MissingId() {
        ProductDTO dto = new ProductDTO();  // No ID

        ResponseEntity<String> response = productService.updateProductDetailsService(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void testDeleteProductDetailsService_Success() {
        Product product = new Product();
        product.setId("abc");

        when(productRepo.findById("abc")).thenReturn(Optional.of(product));

        ResponseEntity<String> response = productService.deleteProductDetailsService("abc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void testDeleteProductDetailsService_MissingId() {
        ResponseEntity<String> response = productService.deleteProductDetailsService(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void testDeleteProductDetailsService_NotFound() {
        when(productRepo.findById("xyz")).thenReturn(Optional.empty());

        ResponseEntity<String> response = productService.deleteProductDetailsService("xyz");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}