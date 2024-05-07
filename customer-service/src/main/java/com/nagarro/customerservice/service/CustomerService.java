package com.nagarro.customerservice.service;

import java.util.List;
import java.util.Optional;

import com.nagarro.customerservice.dto.CustomerRequest;
import com.nagarro.customerservice.dto.CustomerResponse;
import com.nagarro.customerservice.model.Customer;

public interface CustomerService {

	CustomerResponse addCustomer(CustomerRequest customerRequest);
	
	List<CustomerResponse> getAllCustomers();

	Optional<CustomerResponse> getCustomerById(Long id);

	CustomerResponse updateCustomer(Long id, CustomerRequest updatedCustomer);

	boolean deleteCustomer(Long id);

	Optional<Customer> getCustomerByName(String name);
	

}
