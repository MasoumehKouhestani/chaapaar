package ir.chaapaar.project.controller;

import ir.chaapaar.project.dto.CustomerDto;
import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.exception.customer.CustomerNotFoundException;
import ir.chaapaar.project.service.CustomerService;
import ir.chaapaar.project.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
@Slf4j
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Customer save(@RequestBody CustomerDto customer) {
        return customerService.save(customer);
    }

    //TODO: is that ok returning null (load and update and delete)
    @GetMapping(value = "/load/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer load(@PathVariable(value = "id") String id) {
        try {
            return customerService.load(id);
        } catch (CustomerNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Customer with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping(value = "/load-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Customer> loadAll() {
        return customerService.loadAll();
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Customer update(@PathVariable(value = "id") String id, @RequestBody CustomerDto customer) {
        try {
            return customerService.update(id, customer);
        } catch (CustomerNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Customer with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping("/delete/{id}")
    public Customer delete(@PathVariable(value = "id") String id) {
        try {
            return customerService.delete(id);
        } catch (CustomerNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Customer with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping("/delete-all")
    public void delete() {
        customerService.deleteAll();
    }

}
