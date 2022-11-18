package ir.chaapaar.project.entity.customer;

import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.repository.CustomerRepository;
import ir.chaapaar.project.util.CustomerTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ir.chaapaar.project.util.Constants.CUSTOMER_EMAIL_1;
import static ir.chaapaar.project.util.Constants.CUSTOMER_EMAIL_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class CustomerRepositoryUnitTest {

    CustomerTestUtil testUtil = new CustomerTestUtil();
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveAndLoadACustomerSuccessfully() {
        Customer customer = testUtil.createCustomer(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerRepository.save(customer);
        assertEquals(customer.getEmail(), savedCustomer.getEmail());
    }

    @Test
    public void testSaveACustomerFails() {
        // ...
    }

    @Test
    public void testLoadACustomerThatDoesNotExistFails() {
        Optional<Customer> customer = customerRepository.findById(UUID.randomUUID().toString());
        assertFalse(customer.isPresent());
    }

    @Test
    public void testLoadAllCustomers_WhenTwoCustomersAreSaved() {
        customerRepository.save(testUtil.createCustomer(CUSTOMER_EMAIL_1));
        customerRepository.save(testUtil.createCustomer(CUSTOMER_EMAIL_2));
        final List<Customer> allCustomers = customerRepository.findAll();
        assertEquals(2, allCustomers.size());
    }

    @Test
    public void testUpdateACustomerThatExistsSuccessfully() {
        Customer customer = testUtil.createCustomer(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerRepository.save(customer);
        savedCustomer.setEmail(CUSTOMER_EMAIL_2);
        Customer updatedCustomer = customerRepository.save(savedCustomer);
        assertEquals(savedCustomer.getId(), updatedCustomer.getId());
    }

    @Test
    public void testUpdateACustomerThatDoesNotExistFails() {
        // ...
    }

    @Test
    public void testDeleteACustomerThatExistsSuccessfully() {
        Customer customer = testUtil.createCustomer(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerRepository.save(customer);
        customerRepository.delete(savedCustomer);
        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getId());
        assertFalse(deletedCustomer.isPresent());
    }

    @Test
    public void testDeleteACustomerThatDoesNotExistFails() {
        // ...
    }
}
