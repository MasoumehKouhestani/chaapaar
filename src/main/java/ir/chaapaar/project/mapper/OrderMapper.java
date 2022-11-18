package ir.chaapaar.project.mapper;

import ir.chaapaar.project.dto.OrderDto;
import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.entity.OrderId;

public class OrderMapper {

    public static Order mapOrderDtoToEntity(OrderDto orderDto) {
        return Order.builder()
                .id(new OrderId(orderDto.getCustomer(), orderDto.getProduct()))
                .count(orderDto.getCount())
                .build();
    }

    public static OrderDto mapOrderEntityToDto(Order order) {
        return OrderDto.builder()
                .customer(order.getId().getCustomer())
                .product(order.getId().getProduct())
                .count(order.getCount())
                .build();
    }
}
