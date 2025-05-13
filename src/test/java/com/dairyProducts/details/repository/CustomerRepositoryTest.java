package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Save and retrieve customer by card number")
    public void testSaveAndFindByCardNumber() {
        Customer customer = new Customer();
        customer.setCustomerName("John Doe");
        customer.setMobileNo(9876543210L);
        customer.setStatus("ACTIVE");
        customer.setDefaulter("N");

        Customer saved = customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByCardNumber(saved.getCardNumber());

        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getCustomerName());
    }

    @Test
    @DisplayName("Find by mobile number")
    public void testFindByMobileNo() {
        Customer customer = new Customer();
        customer.setCardNumber(654321L);
        customer.setCustomerName("Jane Smith");
        customer.setMobileNo(9999999999L);
        customer.setStatus("ACTIVE");
        customer.setDefaulter("N");

        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByMobileNo(9999999999L);

        assertTrue(found.isPresent());
        assertEquals("Jane Smith", found.get().getCustomerName());
    }

    @Test
    @DisplayName("Find by customer name")
    public void testFindByCustomerName() {
        Customer customer = new Customer();
        customer.setCardNumber(222222L);
        customer.setCustomerName("Alice");
        customer.setMobileNo(8888888888L);
        customer.setStatus("INACTIVE");
        customer.setDefaulter("Y");

        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByCustomerName("Alice");

        assertTrue(found.isPresent());
        assertEquals(20251001L, found.get().getCardNumber());
    }
}
