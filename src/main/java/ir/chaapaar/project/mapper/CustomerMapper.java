package ir.chaapaar.project.mapper;

import ir.chaapaar.project.dto.CustomerDto;
import ir.chaapaar.project.entity.Customer;

public class CustomerMapper {

    public static Customer mapCustomerDtoToEntity(CustomerDto customerDto) {
        return Customer.builder()
                .description(customerDto.getDescription())
                .email(customerDto.getEmail())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .build();
    }

    public static CustomerDto mapCustomerEntityToDto(Customer customer) {
        return CustomerDto.builder()
                .description(customer.getDescription())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
