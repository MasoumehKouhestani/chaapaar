package ir.chaapaar.project.controller;

import ir.chaapaar.project.dto.CustomerDto;
import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.exception.customer.CustomerNotFoundException;
import ir.chaapaar.project.service.CustomerService;
import ir.chaapaar.project.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
@Slf4j
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> save(@RequestBody CustomerDto customer) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.save(customer));
    }

    @GetMapping(value = "/load/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> load(@PathVariable(value = "id") String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(customerService.load(id));
        } catch (CustomerNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Customer with id %s not found!", id)));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "/load-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Customer> loadAll() {
        return customerService.loadAll();
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> update(@PathVariable(value = "id") String id, @RequestBody CustomerDto customer) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(customerService.update(id, customer));
        } catch (CustomerNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Customer with id %s not found!", id)));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Customer> delete(@PathVariable(value = "id") String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(customerService.delete(id));
        } catch (CustomerNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Customer with id %s not found!", id)));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/delete-all")
    public void delete() {
        customerService.deleteAll();
    }

}
