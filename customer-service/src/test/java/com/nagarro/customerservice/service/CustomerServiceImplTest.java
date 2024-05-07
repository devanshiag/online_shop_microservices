package com.nagarro.customerservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nagarro.customerservice.model.Customer;
import com.nagarro.customerservice.repository.CustomerRepository;

//public class CustomerServiceImplTest {
//
//    @InjectMocks
//    private CustomerServiceImpl customerService;
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @SuppressWarnings("deprecation")
//	@BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testAddCustomer() {
//        // Mock data
//        Customer customerToAdd = new Customer();
//        // Set up other required fields in Customer
//        
//        // Mock repository method
//        when(customerRepository.save(any(Customer.class))).thenReturn(customerToAdd);
//        
//        // Call service method
//        Customer addedCustomer = customerService.addCustomer(customerToAdd);
//        
//        // Verify repository method was called
//        verify(customerRepository, times(1)).save(any(Customer.class));
//        
//        // Assert that the addedCustomer is not null
//        assertNotNull(addedCustomer);
//        // Add additional assertions as needed
//    }
//
//    @Test
//    public void testGetAllCustomers() {
//        // Mock data
//        List<Customer> customers = new ArrayList<>();
//        // Add some dummy customers to the list
//        
//        // Mock repository method
//        when(customerRepository.findAll()).thenReturn(customers);
//        
//        // Call service method
//        List<Customer> fetchedCustomers = customerService.getAllCustomers();
//        
//        // Verify repository method was called
//        verify(customerRepository, times(1)).findAll();
//        
//        // Assert that the fetchedCustomers list is not null and has the correct size
//        assertNotNull(fetchedCustomers);
//        assertEquals(customers.size(), fetchedCustomers.size());
//        // Add additional assertions as needed
//    }
//
//    @Test
//    public void testGetCustomerById() {
//        // Mock data
//        Long customerId = 1L;
//        Customer customer = new Customer();
//        // Set up other required fields in Customer
//        
//        // Mock repository method
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
//        
//        // Call service method
//        Optional<Customer> fetchedCustomerOptional = customerService.getCustomerById(customerId);
//        
//        // Verify repository method was called
//        verify(customerRepository, times(1)).findById(customerId);
//        
//        // Assert that the fetchedCustomerOptional is not null and contains the correct customer
//        assertTrue(fetchedCustomerOptional.isPresent());
//        assertEquals(customer, fetchedCustomerOptional.get());
//        // Add additional assertions as needed
//    }
//
//    @Test
//    public void testUpdateCustomer() {
//        // Mock data
//        Long customerId = 1L;
//        Customer existingCustomer = new Customer();
//        // Set up other required fields in existingCustomer
//        
//        Customer updatedCustomer = new Customer();
//        // Set up other required fields in updatedCustomer
//        
//        // Mock repository method
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
//        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
//        
//        // Call service method
//        Customer updatedCustomerResult = customerService.updateCustomer(customerId, updatedCustomer);
//        
//        // Verify repository method was called
//        verify(customerRepository, times(1)).findById(customerId);
//        verify(customerRepository, times(1)).save(any(Customer.class));
//        
//        // Assert that the updatedCustomerResult is not null and has the correct data
//        assertNotNull(updatedCustomerResult);
//        assertEquals(updatedCustomer, updatedCustomerResult);
//        // Add additional assertions as needed
//    }
//
//    @Test
//    public void testDeleteCustomer() {
//        // Mock data
//        Long customerId = 1L;
//        Customer customerToDelete = new Customer();
//        // Set up other required fields in customerToDelete
//        
//        // Mock repository method
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerToDelete));
//        when(customerRepository.save(any(Customer.class))).thenReturn(customerToDelete);
//        
//        // Call service method
//        boolean deleteResult = customerService.deleteCustomer(customerId);
//        
//        // Verify repository method was called
//        verify(customerRepository, times(1)).findById(customerId);
//        verify(customerRepository, times(1)).save(any(Customer.class));
//        
//        // Assert that the deleteResult is true
//        assertTrue(deleteResult);
//        // Add additional assertions as needed
//    }
//}
