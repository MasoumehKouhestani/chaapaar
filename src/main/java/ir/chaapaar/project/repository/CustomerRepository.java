package ir.chaapaar.project.repository;

import ir.chaapaar.project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CustomerRepository extends JpaRepository<Customer, String>, QuerydslPredicateExecutor<Customer> {

    Customer findByEmail(String email);

}
