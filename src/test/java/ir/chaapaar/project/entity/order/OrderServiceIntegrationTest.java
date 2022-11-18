package ir.chaapaar.project.entity.order;

import ir.chaapaar.project.config.OrderConfig;
import ir.chaapaar.project.dto.OrderDto;
import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.entity.OrderId;
import ir.chaapaar.project.exception.order.OrderNotFoundException;
import ir.chaapaar.project.mapper.OrderMapper;
import ir.chaapaar.project.repository.OrderRepository;
import ir.chaapaar.project.service.OrderService;
import ir.chaapaar.project.util.OrderTestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ir.chaapaar.project.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Import(OrderConfig.class)
public class OrderServiceIntegrationTest {

    @Autowired
    OrderTestUtil testUtil;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @Test
    public void testSaveAOrderSuccessfully() {
        OrderDto order = testUtil.createOrderDto(CUSTOMER_EMAIL_1, PRODUCT_NAME_1);
        Order savedOrder = orderService.save(order);
        assertEquals(order.getCustomer().getId(), savedOrder.getId().getCustomer().getId());
    }

    @Test
    public void testSaveAOrderFails() {
        // ...
    }

    @Test
    public void testLoadByIdAOrderThatExistsSuccessfully() {
        OrderDto order = testUtil.createOrderDto(CUSTOMER_EMAIL_1, PRODUCT_NAME_1);
        Order savedOrder = orderService.save(order);
        OrderId orderId = savedOrder.getId();
        Order orderLoadedByService = orderService.load(orderId);
        assertEquals(order.getCustomer().getId(), orderLoadedByService.getId().getCustomer().getId());
    }

    @Test
    public void testLoadByIdAOrderThatDoesNotExistFails() {
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.load(testUtil.createOrderId(CUSTOMER_EMAIL_1, PRODUCT_NAME_1));
        });
    }

    @Test
    public void testLoadAllOrders_WhenTwoOrdersAreSaved() {
        orderService.save(testUtil.createOrderDto(CUSTOMER_EMAIL_1, PRODUCT_NAME_1));
        orderService.save(testUtil.createOrderDto(CUSTOMER_EMAIL_2, PRODUCT_NAME_2));
        final List<Order> allOrders = orderService.loadAll();
        assertEquals(2, allOrders.size());
    }

    @Test
    public void testUpdateAOrderThatExistsSuccessfully() {
        OrderDto order = testUtil.createOrderDto(CUSTOMER_EMAIL_1, PRODUCT_NAME_1);
        Order savedOrder = orderService.save(order);
        savedOrder.setCount(20);
        Order updatedOrder = orderService.update(savedOrder);
        assertEquals(savedOrder.getId(), updatedOrder.getId());
        assertEquals(savedOrder.getCount(), updatedOrder.getCount());
    }

    @Test
    public void testUpdateAOrderThatDoesNotExistFails() {
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.update(testUtil.createOrder(testUtil.createOrderId(CUSTOMER_EMAIL_1, PRODUCT_NAME_1)));
        });
    }

    @Test
    public void testDeleteAOrderThatExistsSuccessfully() {
        OrderDto order = testUtil.createOrderDto(CUSTOMER_EMAIL_1, PRODUCT_NAME_1);
        Order savedOrder = orderService.save(order);
        orderService.delete(savedOrder.getId());
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.load(savedOrder.getId());
        });
    }

    @Test
    public void testDeleteAOrderThatDoesNotExistFails() {
        OrderDto order = testUtil.createOrderDto(CUSTOMER_EMAIL_1, PRODUCT_NAME_1);
        Order savedOrder = orderService.save(order);
        orderService.delete(savedOrder.getId());
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.delete(savedOrder.getId());
        });
    }
}
