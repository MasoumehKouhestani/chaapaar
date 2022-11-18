package ir.chaapaar.project.config;

import ir.chaapaar.project.repository.CustomerRepository;
import ir.chaapaar.project.repository.ProductRepository;
import ir.chaapaar.project.util.OrderTestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderConfig {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @Bean
    public OrderTestUtil orderTestUtil() {
        OrderTestUtil orderTestUtil = new OrderTestUtil();
        orderTestUtil.setCustomerRepository(customerRepository);
        orderTestUtil.setProductRepository(productRepository);
        return orderTestUtil;
    }
}
