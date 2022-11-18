package ir.chaapaar.project.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ir.chaapaar.project.dto.OrderDto;
import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.entity.OrderId;
import ir.chaapaar.project.exception.order.OrderNotFoundException;
import ir.chaapaar.project.service.OrderService;
import ir.chaapaar.project.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Order save(@RequestBody OrderDto order) {
        return orderService.save(order);
    }

    //TODO: is that ok returning null (load and update and delete)
    @PostMapping(value = "/load", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Order load(@RequestBody OrderId id) {
        try {
            return orderService.load(id);
        } catch (OrderNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Order with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping(value = "/load-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> loadAll() {
        return orderService.loadAll();
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Order update(@RequestBody Order order) {
        try {
            return orderService.update(order);
        } catch (OrderNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Order with id %s not found!", order.getId())));
            return null;
        }
    }

    @PostMapping("/delete")
    public Order delete(@RequestBody OrderId id) {
        try {
            return orderService.delete(id);
        } catch (OrderNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Order with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping("/delete-all")
    public void delete() {
        orderService.deleteAll();
    }
}
