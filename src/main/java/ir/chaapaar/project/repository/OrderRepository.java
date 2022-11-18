package ir.chaapaar.project.repository;

import ir.chaapaar.project.entity.Order;
import ir.chaapaar.project.entity.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface OrderRepository extends JpaRepository<Order, OrderId>, QuerydslPredicateExecutor<Order> {

}
