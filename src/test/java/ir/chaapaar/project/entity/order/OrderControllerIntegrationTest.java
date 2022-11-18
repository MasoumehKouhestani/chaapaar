package ir.chaapaar.project.entity.order;

import ir.chaapaar.project.config.OrderConfig;
import ir.chaapaar.project.controller.CustomerController;
import ir.chaapaar.project.controller.OrderController;
import ir.chaapaar.project.controller.ProductController;
import ir.chaapaar.project.dto.OrderDto;
import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.entity.OrderId;
import ir.chaapaar.project.entity.Product;
import ir.chaapaar.project.mapper.OrderMapper;
import ir.chaapaar.project.repository.OrderRepository;
import ir.chaapaar.project.util.OrderTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static ir.chaapaar.project.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(OrderConfig.class)
public class OrderControllerIntegrationTest {

    @Autowired
    OrderTestUtil testUtil;
    @LocalServerPort
    private int port;
    private String baseUrl = "";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderController orderController;
    @Autowired
    private CustomerController customerController;
    @Autowired
    private ProductController productController;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/chaapaar/order";
    }

    public Customer createAndSaveCustomerByController(String email) {
        return restTemplate.postForEntity("http://localhost:" + port + "/chaapaar/customer/save",
                testUtil.getCustomerTestUtil().createCustomer(email), Customer.class).getBody();
    }

    public Product createAndSaveProductByController(String name) {
        return restTemplate.postForEntity("http://localhost:" + port + "/chaapaar/product/save",
                testUtil.getProductTestUtil().createProduct(name), Product.class).getBody();
    }

    @Test
    public void testSaveAnOrderSuccessfully() {
        OrderDto order = testUtil.createOrderDto(createAndSaveCustomerByController(CUSTOMER_EMAIL_1), createAndSaveProductByController(PRODUCT_NAME_1));
        ResponseEntity<Order> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", order, Order.class);
        assertEquals(HttpStatus.OK, saveRequestResponse.getStatusCode());

        Optional<Order> loadedOrder = orderRepository.findById(saveRequestResponse.getBody().getId());
        assertTrue(loadedOrder.isPresent());
        assertEquals(order.getCount(), loadedOrder.get().getCount());
    }

    @Test
    public void testSaveAnOrderFails() {
        // ...
    }

    @Test
    public void testLoadByIdAnOrderThatExistsSuccessfully() {
        OrderDto order = testUtil.createOrderDto(createAndSaveCustomerByController(CUSTOMER_EMAIL_2), createAndSaveProductByController(PRODUCT_NAME_2));
        ResponseEntity<Order> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", order, Order.class);
        OrderId orderId = saveRequestResponse.getBody().getId();
        ResponseEntity<Order> loadRequestResponse = restTemplate.postForEntity(baseUrl + "/load", orderId, Order.class);
        assertEquals(HttpStatus.OK, loadRequestResponse.getStatusCode());
        assertEquals(orderId, loadRequestResponse.getBody().getId());
    }

    @Test
    public void testLoadByIdAnOrderThatDoesNotExistFails() {
        OrderId randomOrderId = testUtil.createOrderId(CUSTOMER_EMAIL_3, PRODUCT_NAME_3);
        ResponseEntity<Order> loadRequestResponse = restTemplate.postForEntity(baseUrl + "/load", randomOrderId, Order.class);
        assertNull(loadRequestResponse.getBody());
    }

    @Test
    public void testLoadAllOrders_WhenTwoOrdersAreSaved() {
        restTemplate.execute(baseUrl + "/delete-all", HttpMethod.GET, null, null);
        OrderDto order1 = testUtil.createOrderDto(createAndSaveCustomerByController(CUSTOMER_EMAIL_4), createAndSaveProductByController(PRODUCT_NAME_4));
        restTemplate.postForEntity(baseUrl + "/save", order1, Order.class);
        OrderDto order2 = testUtil.createOrderDto(createAndSaveCustomerByController(CUSTOMER_EMAIL_5), createAndSaveProductByController(PRODUCT_NAME_5));
        restTemplate.postForEntity(baseUrl + "/save", order2, Order.class);
        ResponseEntity<Order[]> response = restTemplate.getForEntity(baseUrl + "/load-all/", Order[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testUpdateAnOrderThatExistsSuccessfully() {
        OrderDto order = testUtil.createOrderDto(createAndSaveCustomerByController(CUSTOMER_EMAIL_6), createAndSaveProductByController(PRODUCT_NAME_6));
        ResponseEntity<Order> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", order, Order.class);
        Order savedOrder = saveRequestResponse.getBody();
        savedOrder.setCount(20);
        ResponseEntity<Order> updatedRequestResponse = restTemplate
                .postForEntity(baseUrl + "/update", savedOrder, Order.class);
        assertEquals(HttpStatus.OK, updatedRequestResponse.getStatusCode());
        assertEquals(savedOrder.getId(), updatedRequestResponse.getBody().getId());
        assertEquals(savedOrder.getCount(), updatedRequestResponse.getBody().getCount());
    }

    @Test
    public void testUpdateAnOrderThatDoesNotExistFails() {
        ResponseEntity<Order> updatedRequestResponse = restTemplate
                .postForEntity(baseUrl + "/update",
                        testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_7, PRODUCT_NAME_7)), Order.class);
        assertEquals(HttpStatus.NOT_FOUND, updatedRequestResponse.getStatusCode());
        assertNull(updatedRequestResponse.getBody());
    }

    @Test
    public void testDeleteAnOrderThatExistsSuccessfully() {
        OrderDto order = testUtil.createOrderDto(createAndSaveCustomerByController(CUSTOMER_EMAIL_8), createAndSaveProductByController(PRODUCT_NAME_8));
        ResponseEntity<Order> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", order, Order.class);
        Order savedOrder = saveRequestResponse.getBody();
        ResponseEntity<Order> deleteRequestResponse = restTemplate.postForEntity(baseUrl + "/delete", savedOrder.getId(), Order.class);
        assertEquals(HttpStatus.OK, deleteRequestResponse.getStatusCode());

        assertFalse(orderRepository.findById(savedOrder.getId()).isPresent());
    }

    @Test
    public void testDeleteAnOrderThatDoesNotExistFails() {
        ResponseEntity<Order> deleteRequestResponse = restTemplate.postForEntity(baseUrl + "/delete", testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_7, PRODUCT_NAME_7)), Order.class);
        assertEquals(HttpStatus.NOT_FOUND, deleteRequestResponse.getStatusCode());
        assertNull(deleteRequestResponse.getBody());
    }
}
