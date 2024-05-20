package com.nagarro.customerservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.customerservice.dto.CustomerRequest;
import com.nagarro.customerservice.dto.CustomerResponse;
import com.nagarro.customerservice.exception.CustomerAlreadyExistsException;
import com.nagarro.customerservice.exception.CustomerNotFoundException;
import com.nagarro.customerservice.model.Customer;
import com.nagarro.customerservice.service.CustomerService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
	
    @Autowired
    private CustomerService customerService;

    
    @PostMapping("/")
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody CustomerRequest customerRequest) {

        Optional<Customer> existingCustomer = customerService.getCustomerByName(customerRequest.getName());

        if (existingCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer with name " + customerRequest.getName() + " already exists.");
        }

        CustomerResponse savedCustomer = customerService.addCustomer(customerRequest);

        log.info("Customer with ID {} created successfully", savedCustomer.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }


    @PreAuthorize("hasRole('client_admin')")
    @GetMapping("/all")
    public ResponseEntity<Object> getAllCustomers() {
            List<CustomerResponse> customers = customerService.getAllCustomers();
            
            if (customers.isEmpty()) {
            	log.info("No customers found");;
                throw new CustomerNotFoundException("No customers exist.");
            }

            log.info("Successfully fetched all customers");
            return ResponseEntity.status(HttpStatus.OK).body(customers);
            
        
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id) {
        Optional<CustomerResponse> searchedCustomer = customerService.getCustomerById(id);
        if (!searchedCustomer.isPresent()) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        log.info("Customer with ID {} fetched successfully", id);
        return ResponseEntity.status(HttpStatus.OK).body(searchedCustomer.get());
    }

    @PreAuthorize("hasRole('client_user')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Updating customer with ID: {}", id);
        Optional<CustomerResponse> existingCustomer = customerService.getCustomerById(id);
        if (!existingCustomer.isPresent()) {
        	throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        CustomerResponse updatedCustomer = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer with ID: {}", id);
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            String message = "Customer with ID " + id + " has been deleted";
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

}

