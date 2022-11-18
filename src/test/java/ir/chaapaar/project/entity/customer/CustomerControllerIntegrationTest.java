package ir.chaapaar.project.entity.customer;

import ir.chaapaar.project.controller.CustomerController;
import ir.chaapaar.project.dto.CustomerDto;
import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.mapper.CustomerMapper;
import ir.chaapaar.project.repository.CustomerRepository;
import ir.chaapaar.project.service.CustomerService;
import ir.chaapaar.project.util.CustomerTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static ir.chaapaar.project.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;
    private String baseUrl = "";

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerController customerController;

    @Autowired
    private TestRestTemplate restTemplate;

    CustomerTestUtil testUtil = new CustomerTestUtil();

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/chaapaar/customer";
    }

    @Test
    public void testSaveACustomerSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_1);
        ResponseEntity<Customer> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", customer, Customer.class);
        assertEquals(HttpStatus.OK, saveRequestResponse.getStatusCode());

        Customer loadedCustomer = customerRepository.findByEmail(CUSTOMER_EMAIL_1);
        assertEquals(customer.getEmail(), loadedCustomer.getEmail());
    }

    @Test
    public void testSaveACustomerFails() {
        // ...
    }

    @Test
    public void testLoadByIdACustomerThatExistsSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_2);
        ResponseEntity<Customer> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", customer, Customer.class);
        String customerId = saveRequestResponse.getBody().getId();
        ResponseEntity<Customer> loadRequestResponse = restTemplate.getForEntity(baseUrl + "/load/" + customerId, Customer.class);
        assertEquals(HttpStatus.OK, loadRequestResponse.getStatusCode());
        assertEquals(customerId, loadRequestResponse.getBody().getId());
        assertEquals(CUSTOMER_EMAIL_2, loadRequestResponse.getBody().getEmail());
    }

    @Test
    public void testLoadByIdACustomerThatDoesNotExistFails() {
        ResponseEntity<Customer> loadRequestResponse = restTemplate.getForEntity(baseUrl + "/load/" + UUID.randomUUID(), Customer.class);
        assertNull(loadRequestResponse.getBody());
    }

    @Test
    public void testLoadAllCustomers_WhenTwoCustomersAreSaved() {
        restTemplate.execute(baseUrl + "/delete-all", HttpMethod.GET, null, null);
        CustomerDto customer1 = testUtil.createCustomerDto(CUSTOMER_EMAIL_3);
        restTemplate.postForEntity(baseUrl + "/save", customer1, Customer.class);
        CustomerDto customer2 = testUtil.createCustomerDto(CUSTOMER_EMAIL_4);
        restTemplate.postForEntity(baseUrl + "/save", customer2, Customer.class);
        ResponseEntity<Customer[]> response = restTemplate.getForEntity(baseUrl + "/load-all/", Customer[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testUpdateACustomerThatExistsSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_5);
        ResponseEntity<Customer> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", customer, Customer.class);
        Customer savedCustomer = saveRequestResponse.getBody();
        savedCustomer.setEmail(CUSTOMER_EMAIL_6);
        ResponseEntity<Customer> updatedRequestResponse = restTemplate.postForEntity(baseUrl + "/update/" + savedCustomer.getId(),
                CustomerMapper.mapCustomerEntityToDto(savedCustomer), Customer.class);
        assertEquals(HttpStatus.OK, updatedRequestResponse.getStatusCode());
        assertEquals(savedCustomer.getId(), updatedRequestResponse.getBody().getId());
        assertEquals(CUSTOMER_EMAIL_6, updatedRequestResponse.getBody().getEmail());
    }

    @Test
    public void testUpdateACustomerThatDoesNotExistFails() {
        ResponseEntity<Customer> updatedRequestResponse = restTemplate.postForEntity(baseUrl + "/update/" + UUID.randomUUID(),
                testUtil.createCustomerDto(CUSTOMER_EMAIL_1), Customer.class);
        assertEquals(HttpStatus.NOT_FOUND, updatedRequestResponse.getStatusCode());
        assertNull(updatedRequestResponse.getBody());
    }

    @Test
    public void testDeleteACustomerThatExistsSuccessfully() {
        CustomerDto customer = testUtil.createCustomerDto(CUSTOMER_EMAIL_7);
        ResponseEntity<Customer> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", customer, Customer.class);
        Customer savedCustomer = saveRequestResponse.getBody();
        ResponseEntity<Customer> deleteRequestResponse = restTemplate.getForEntity(baseUrl + "/delete/" + savedCustomer.getId(), Customer.class);
        assertEquals(HttpStatus.OK, deleteRequestResponse.getStatusCode());
        assertFalse(customerRepository.findById(savedCustomer.getId()).isPresent());
    }

    @Test
    public void testDeleteACustomerThatDoesNotExistFails() {
        ResponseEntity<Customer> deleteRequestResponse = restTemplate.getForEntity(baseUrl + "/delete/" + UUID.randomUUID(), Customer.class);
        assertEquals(HttpStatus.NOT_FOUND, deleteRequestResponse.getStatusCode());
        assertNull(deleteRequestResponse.getBody());
    }
}
