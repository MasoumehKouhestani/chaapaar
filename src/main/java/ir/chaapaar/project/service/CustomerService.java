package ir.chaapaar.project.service;

import ir.chaapaar.project.dto.CustomerDto;
import ir.chaapaar.project.entity.Customer;
import ir.chaapaar.project.exception.customer.CustomerNotFoundException;
import ir.chaapaar.project.mapper.CustomerMapper;
import ir.chaapaar.project.repository.CustomerRepository;
import ir.chaapaar.project.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    public Customer save(CustomerDto customerDto) {
        Customer customer = customerRepository.save(CustomerMapper.mapCustomerDtoToEntity(customerDto));
        log.info(LogUtils.encode(String.format("Customer with email «%s» saved.", customer.getEmail())));
        return customer;
    }

    @Transactional
    public Customer load(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException(String.format("Customer with id %s not found!", id));
        }
        log.info(LogUtils.encode(String.format("Customer with id «%s» loaded.", id)));
        return customer.get();
    }

    @Transactional
    public List<Customer> loadAll() {
        List<Customer> customers = customerRepository.findAll();
        log.info(LogUtils.encode("All customers loaded."));
        return customers;
    }

    @Transactional
    public Customer update(String id, CustomerDto customerDto) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customer newCustomer = CustomerMapper.mapCustomerDtoToEntity(customerDto);
            newCustomer.setId(customer.get().getId());
            Customer updatedCustomer = customerRepository.save(newCustomer);
            log.info(LogUtils.encode(String.format("Customer with id «%s» updates.", updatedCustomer.getId())));
            return updatedCustomer;
        } else {
            throw new CustomerNotFoundException(String.format("Customer with id %s not found!", id));
        }
    }

    @Transactional
    public Customer delete(String id) {
        final Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException(String.format("Customer with id %s not found!", id));
        }
        customerRepository.delete(customer.get());
        log.info(LogUtils.encode(String.format("Customer with id «%s» deleted.", id)));
        return customer.get();
    }

    @Transactional
    public void deleteAll() {
        customerRepository.deleteAll();
    }
}
