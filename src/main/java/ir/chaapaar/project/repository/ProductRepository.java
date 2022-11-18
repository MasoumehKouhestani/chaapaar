package ir.chaapaar.project.repository;

import ir.chaapaar.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductRepository extends JpaRepository<Product, Integer>, QuerydslPredicateExecutor<Product> {

    Product findByName(String name);
}
