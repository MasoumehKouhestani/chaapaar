package ir.chaapaar.project.entity.order;

import ir.chaapaar.project.config.OrderConfig;
import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.repository.OrderRepository;
import ir.chaapaar.project.util.OrderTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static ir.chaapaar.project.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@Import(OrderConfig.class)
public class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderTestUtil testUtil;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testSaveAndLoadAnOrderSuccessfully() {
        Order order = testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_1, PRODUCT_NAME_1));
        Order savedOrder = orderRepository.save(order);
        assertEquals(order.getId(), savedOrder.getId());
    }

    @Test
    public void testSaveAnOrderFails() {
        // ...
    }

    @Test
    public void testLoadAnOrderThatDoesNotExistFails() {
        Optional<Order> order = orderRepository.findById(testUtil.createOrderId(CUSTOMER_EMAIL_1, PRODUCT_NAME_1));
        assertFalse(order.isPresent());
    }

    @Test
    public void testLoadAllOrders_WhenTwoOrdersAreSaved() {
        orderRepository.save(testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_1, PRODUCT_NAME_1)));
        orderRepository.save(testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_2, PRODUCT_NAME_2)));
        final List<Order> allOrders = orderRepository.findAll();
        assertEquals(2, allOrders.size());
    }

    @Test
    public void testUpdateAnOrderThatExistsSuccessfully() {
        Order order = testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_1, PRODUCT_NAME_1));
        Order savedOrder = orderRepository.save(order);
        savedOrder.setCount(20);
        Order updatedOrder = orderRepository.save(savedOrder);
        assertEquals(savedOrder.getCount(), updatedOrder.getCount());
    }

    @Test
    public void testUpdateAnOrderThatDoesNotExistFails() {
        // ...
    }

    @Test
    public void testDeleteAnOrderThatExistsSuccessfully() {
        Order order = testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_1, PRODUCT_NAME_1));
        Order savedOrder = orderRepository.save(order);
        orderRepository.delete(savedOrder);
        Optional<Order> deletedOrder = orderRepository.findById(savedOrder.getId());
        assertFalse(deletedOrder.isPresent());
    }

    @Test
    public void testDeleteAnOrderThatDoesNotExistFails() {
        // ...
    }
}
