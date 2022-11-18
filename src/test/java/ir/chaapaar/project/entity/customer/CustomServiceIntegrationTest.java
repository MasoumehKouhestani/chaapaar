package ir.chaapaar.project.entity.customer;

import ir.chaapaar.project.dto.CustomerDto;
import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.exception.customer.CustomerNotFoundException;
import ir.chaapaar.project.mapper.CustomerMapper;
import ir.chaapaar.project.repository.CustomerRepository;
import ir.chaapaar.project.service.CustomerService;
import ir.chaapaar.project.util.CustomerTestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static ir.chaapaar.project.util.Constants.CUSTOMER_EMAIL_1;
import static ir.chaapaar.project.util.Constants.CUSTOMER_EMAIL_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class CustomServiceIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    CustomerTestUtil testUtil = new CustomerTestUtil();

    @Test
    public void testSaveACustomerSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerService.save(customer);
        assertEquals(customer.getEmail(), savedCustomer.getEmail());
    }

    @Test
    public void testSaveACustomerFails() {
        // ...
    }

    @Test
    public void testLoadByIdACustomerThatExistsSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerService.save(customer);
        String customerId = savedCustomer.getId();
        Customer customerLoadedByService = customerService.load(customerId);
        assertEquals(customer.getEmail(), customerLoadedByService.getEmail());
    }

    @Test
    public void testLoadByIdACustomerThatDoesNotExistFails() {
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.load(UUID.randomUUID().toString());
        });
    }

    @Test
    public void testLoadAllCustomers_WhenTwoCustomersAreSaved() {
        customerService.save(testUtil.createCustomerDto(CUSTOMER_EMAIL_1));
        customerService.save(testUtil.createCustomerDto(CUSTOMER_EMAIL_2));
        final List<Customer> allCustomers = customerService.loadAll();
        assertEquals(2, allCustomers.size());
    }

    @Test
    public void testUpdateACustomerThatExistsSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerService.save(customer);
        savedCustomer.setEmail(CUSTOMER_EMAIL_2);
        Customer updatedCustomer = customerService.update(savedCustomer.getId(), CustomerMapper.mapCustomerEntityToDto(savedCustomer));
        assertEquals(savedCustomer.getId(), updatedCustomer.getId());
        assertEquals(CUSTOMER_EMAIL_2, updatedCustomer.getEmail());
    }

    @Test
    public void testUpdateACustomerThatDoesNotExistFails() {
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.update(UUID.randomUUID().toString(), testUtil.createCustomerDto(CUSTOMER_EMAIL_1));
        });
    }

    @Test
    public void testDeleteACustomerThatExistsSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerService.save(customer);
        customerService.delete(savedCustomer.getId());
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.load(savedCustomer.getId());
        });
    }

    @Test
    public void testDeleteACustomerThatDoesNotExistFails() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_1);
        Customer savedCustomer = customerService.save(customer);
        customerService.delete(savedCustomer.getId());
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.delete(savedCustomer.getId());
        });
    }

}
