package com.dairyProducts.details.controller;

import com.dairyProducts.details.dto.CustomerDTO;
import com.dairyProducts.details.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDTO customerDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = new CustomerDTO();
        customerDTO.setCardNumber(123456L);
        customerDTO.setCustomerName("John Doe");
        customerDTO.setAddressLine1("Street 1");
        customerDTO.setAddressLine2("Street 2");
        customerDTO.setArea(123456);
        customerDTO.setMobileNo(9876543210L);
        customerDTO.setDefaulter("N");
        customerDTO.setStatus("ACTIVE");
    }

    @Test
    public void testAddCustomer() throws Exception {
        when(customerService.addCustomerDetailsService(any())).thenReturn(ResponseEntity.ok("Added"));

        ResponseEntity<String> response = customerController.addCustomer(customerDTO, "test-correlation-id");

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService).addCustomerDetailsService(any());
    }

    @Test
    public void testGetCustomerDetails() {
        when(customerService.getCustomerDetailsService(123456L)).thenReturn((ResponseEntity) ResponseEntity.ok("Customer"));

        ResponseEntity<?> response = customerController.getCustomerDetails(123456L);

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService).getCustomerDetailsService(123456L);
    }

    @Test
    public void testUpdateCustomerDetails() {
        when(customerService.updateCustomerDetailsService(any())).thenReturn(ResponseEntity.ok("Updated"));

        ResponseEntity<String> response = customerController.updateCustomerDetails(123456L, customerDTO, "corr-id");

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService).updateCustomerDetailsService(any());
    }

    @Test
    public void testDeleteCustomerDetails() {
        when(customerService.deleteCustomerDetailsService(123456L)).thenReturn(ResponseEntity.ok("Deleted"));

        ResponseEntity<String> response = customerController.deleteCustomerDetails(123456L, "corr-id");

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService).deleteCustomerDetailsService(123456L);
    }
}
