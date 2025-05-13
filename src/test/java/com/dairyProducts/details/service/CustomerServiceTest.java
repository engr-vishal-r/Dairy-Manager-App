package com.dairyProducts.details.service;

import com.dairyProducts.details.dto.CustomerDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepo;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerDTO = new CustomerDTO();
        customerDTO.setCardNumber(123456L);
        customerDTO.setCustomerName("John Doe");
        customerDTO.setMobileNo(9876543210L);
        customerDTO.setArea(123456);
        customerDTO.setStatus("ACTIVE");
        customerDTO.setDefaulter("N");
        customerDTO.setAddressLine1("Line1");
        customerDTO.setAddressLine2("Line2");
    }

    @Test
    void testAddCustomer_Success() {
        when(customerRepo.findByMobileNo(customerDTO.getMobileNo())).thenReturn(Optional.empty());
        when(customerRepo.findByCustomerName(customerDTO.getCustomerName())).thenReturn(Optional.empty());

        Customer savedCustomer = new Customer();
        savedCustomer.setCardNumber(123456L);
        when(customerRepo.save(any(Customer.class))).thenReturn(savedCustomer);

        ResponseEntity<String> response = customerService.addCustomerDetailsService(customerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddCustomer_AlreadyExists() {
        when(customerRepo.findByMobileNo(anyLong())).thenReturn(Optional.of(new Customer()));

        ResponseEntity<String> response = customerService.addCustomerDetailsService(customerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("already exist"));
    }

    @Test
    void testGetCustomerDetails_Found() {
        Customer customer = new Customer();
        customer.setCustomerName("John Doe");

        when(customerRepo.findByCardNumber(123456L)).thenReturn(Optional.of(customer));

        ResponseEntity<?> response = customerService.getCustomerDetailsService(123456L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetCustomerDetails_NotFound() {
        when(customerRepo.findByCardNumber(123456L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = customerService.getCustomerDetailsService(123456L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void testGetCustomer_InternalServerError() {
        when(customerRepo.findByMobileNo(anyLong())).thenReturn(Optional.empty());
        when(customerRepo.findByCustomerName(anyString())).thenReturn(Optional.empty());

        // Force customerRepo.get to throw a RuntimeException
        when(customerRepo.findByCardNumber(123456L)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = customerService.getCustomerDetailsService(123456L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error Occurred while adding customer details"));
    }

    @Test
    void testUpdateCustomerDetails_Success() {
        Customer customer = new Customer();
        when(customerRepo.findByCardNumber(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);

        ResponseEntity<String> response = customerService.updateCustomerDetailsService(customerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateCustomerDetails_InvalidDefaulter() {
        customerDTO.setDefaulter("X");
        customerDTO.setStatus("ACTIVE");

        when(customerRepo.findByCardNumber(anyLong())).thenReturn(Optional.of(new Customer()));

        ResponseEntity<String> response = customerService.updateCustomerDetailsService(customerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Defaulter should be"));
    }

    @Test
    void testUpdateCustomerDetails_InvalidStatus() {
        customerDTO.setStatus("UNKNOWN");

        when(customerRepo.findByCardNumber(anyLong())).thenReturn(Optional.of(new Customer()));

        ResponseEntity<String> response = customerService.updateCustomerDetailsService(customerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Status field"));
    }

    @Test
    void testUpdateCustomerDetails_NotFound() {
        when(customerRepo.findByCardNumber(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<String> response = customerService.updateCustomerDetailsService(customerDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void testDeleteCustomerDetails_Success() {
        Customer customer = new Customer();
        when(customerRepo.findByCardNumber(123456L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepo).delete(any(Customer.class));

        ResponseEntity<String> response = customerService.deleteCustomerDetailsService(123456L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("deleted successfully"));
    }

    @Test
    void testDeleteCustomerDetails_NotFound() {
        when(customerRepo.findByCardNumber(123456L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = customerService.deleteCustomerDetailsService(123456L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void testAddCustomer_InvalidMobileNumber() {
        customerDTO.setMobileNo(123L);  // Not 10 digits
        ResponseEntity<String> response = customerService.addCustomerDetailsService(customerDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Mobile no. should be 10 digits"));
    }
    @Test
    void testAddCustomer_InternalServerError() {
        when(customerRepo.findByMobileNo(anyLong())).thenReturn(Optional.empty());
        when(customerRepo.findByCustomerName(anyString())).thenReturn(Optional.empty());

        // Force customerRepo.save to throw a RuntimeException
        when(customerRepo.save(any(Customer.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<String> response = customerService.addCustomerDetailsService(customerDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error Occurred while adding customer details"));
    }

}
