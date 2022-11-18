package ir.chaapaar.project.util;

import ir.chaapaar.project.dto.OrderDto;
import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.entity.OrderId;
import ir.chaapaar.project.entity.Product;
import ir.chaapaar.project.repository.CustomerRepository;
import ir.chaapaar.project.repository.ProductRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@Data
public class OrderTestUtil {

    public static final int ORDER_COUNT = 10;
    Random random = new Random();
    private CustomerTestUtil customerTestUtil = new CustomerTestUtil();
    private ProductTestUtil productTestUtil = new ProductTestUtil();
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    public Customer createAndSaveCustomer(String email) {
        Customer customer = customerTestUtil.createCustomer(email);
        return customerRepository.save(customer);
    }

    public Product createAndSaveProduct(String name) {
        Product product = productTestUtil.createProduct(name);
        return productRepository.save(product);
    }

    public OrderDto createOrderDto(String customerEmail, String productName) {
        return OrderDto.builder()
                .customer(createAndSaveCustomer(customerEmail))
                .product(createAndSaveProduct(productName))
                .count(ORDER_COUNT)
                .build();
    }

    public OrderDto createOrderDto(Customer customer, Product product) {
        return OrderDto.builder()
                .customer(customer)
                .product(product)
                .count(ORDER_COUNT)
                .build();
    }

    public Order createOrder(OrderId orderId) {
        return Order.builder()
                .id(orderId)
                .count(ORDER_COUNT)
                .build();
    }

    public OrderId createOrderId(String customerEmail, String productName) {
        return new OrderId(createAndSaveCustomer(customerEmail), createAndSaveProduct(productName));
    }

}
