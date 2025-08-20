package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Customer;
import com.erp.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
public class CustomerController implements ICommonController<Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @GetMapping
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable(value = "id") Long customerId) {
        Customer customer = customerRepository.getById(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @Override
    @PostMapping
    public Customer save(@Validated @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable(value = "id") Long customerId,
                                           @Validated @RequestBody Customer customerDetails) {
        Customer customer = customerRepository.getById(customerId);

        customer.setCustomerCode(customerDetails.getCustomerCode());
        customer.setCustomerName(customerDetails.getCustomerName());
        customer.setContactPerson(customerDetails.getContactPerson());
        customer.setPhone(customerDetails.getPhone());
        customer.setEmail(customerDetails.getEmail());
        customer.setAddress(customerDetails.getAddress());
        customer.setTaxId(customerDetails.getTaxId());
        customer.setStatus(customerDetails.getStatus());

        final Customer updatedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long customerId) {
        Customer customer = customerRepository.getById(customerId);
        customerRepository.delete(customer);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
