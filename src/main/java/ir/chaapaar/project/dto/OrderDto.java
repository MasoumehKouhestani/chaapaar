package ir.chaapaar.project.dto;

import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.entity.Product;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDto implements Serializable {

    Customer customer;
    Product product;
    Integer count;
}

