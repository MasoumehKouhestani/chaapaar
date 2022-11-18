package ir.chaapaar.project.service;

import ir.chaapaar.project.dto.OrderDto;
import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.entity.OrderId;
import ir.chaapaar.project.exception.order.OrderNotFoundException;
import ir.chaapaar.project.mapper.OrderMapper;
import ir.chaapaar.project.repository.OrderRepository;
import ir.chaapaar.project.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    //TODO: change log levels to debug
    @Transactional
    public Order save(OrderDto orderDto) {
        Order order = orderRepository.save(OrderMapper.mapOrderDtoToEntity(orderDto));
        log.warn(LogUtils.encode("Order saved."));
        return order;
    }

    @Transactional
    public Order load(OrderId id) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            throw new OrderNotFoundException(String.format("Order with id %s not found!", id));
        }
        log.warn(LogUtils.encode(String.format("Order with id «%s» loaded.", id)));
        return order.get();
    }

    @Transactional
    public List<Order> loadAll() {
        List<Order> orders = orderRepository.findAll();
        log.warn(LogUtils.encode("All orders loaded."));
        return orders;
    }

    @Transactional
    public Order update(Order newOrder) {
        Optional<Order> oldOrder = orderRepository.findById(newOrder.getId());
        if (oldOrder.isPresent()) {
            newOrder.setId(oldOrder.get().getId());
            Order updatedOrder = orderRepository.save(newOrder);
            log.warn(LogUtils.encode(String.format("Order with id «%s» updates.", updatedOrder.getId())));
            return updatedOrder;
        } else {
            throw new OrderNotFoundException(String.format("Order with id %s not found!", newOrder.getId()));
        }
    }

    @Transactional
    public Order delete(OrderId id) {
        final Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            throw new OrderNotFoundException(String.format("Order with id %s not found!", id));
        }
        orderRepository.delete(order.get());
        log.warn(LogUtils.encode(String.format("Order with id «%s» deleted.", id)));
        return order.get();
    }

    @Transactional
    public void deleteAll() {
        orderRepository.deleteAll();
    }
}
