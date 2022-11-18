package ir.chaapaar.project.util;

import ir.chaapaar.project.dto.CustomerDto;
import ir.chaapaar.project.entity.Customer;

public class CustomerTestUtil {

    public static final String CUSTOMER_FIRST_NAME = "customerFirstName";
    public static final String CUSTOMER_LAST_NAME = "customerLastName";
    public static final String CUSTOMER_DESCRIPTION = "customerDescription";

    public CustomerDto createCustomerDto(String email) {
        return CustomerDto.builder()
                .firstName(CUSTOMER_FIRST_NAME)
                .lastName(CUSTOMER_LAST_NAME)
                .email(email)
                .description(CUSTOMER_DESCRIPTION)
                .build();
    }

    public Customer createCustomer(String email) {
        return Customer.builder()
                .firstName(CUSTOMER_FIRST_NAME)
                .lastName(CUSTOMER_LAST_NAME)
                .email(email)
                .description(CUSTOMER_DESCRIPTION)
                .build();
    }
}
