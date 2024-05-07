package com.nagarro.customerservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.customerservice.dto.CustomerRequest;
import com.nagarro.customerservice.dto.CustomerResponse;
import com.nagarro.customerservice.model.Customer;
import com.nagarro.customerservice.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService{
	
//	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
    private CustomerRepository customerRepository;

	@Override
	public CustomerResponse addCustomer(CustomerRequest customerRequest) {
		Customer customer = Customer.builder()
				.name(customerRequest.getName())
				.age(customerRequest.getAge())
				.wallet_balance(customerRequest.getWallet_balance())
				.active(true)
				.build();
		
		customerRepository.save(customer);
		log.info("Saved the customer with id {}", customer.getId());

		return mapToCustomerResponse(customer);
		
		
	}

	@Override
	public List<CustomerResponse> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		
		return customers.stream().map(customer -> mapToCustomerResponse(customer)).toList();
	}

	public CustomerResponse mapToCustomerResponse(Customer customer) {
		return CustomerResponse.builder()
				.id(customer.getId())
				.name(customer.getName())
				.age(customer.getAge())
				.wallet_balance(customer.getWallet_balance())
				.active(customer.isActive())
				.build();
		
	}

	@Override
    public Optional<CustomerResponse> getCustomerById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(p -> mapToCustomerResponse(p));
    }

	@Override
	public Optional<Customer> getCustomerByName(String name) {
		return customerRepository.findByName(name);
	}
    

	@Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest updatedCustomer) {
        Optional<Customer> existingCustomerOptional = customerRepository.findById(id);

        Customer existingCustomer = existingCustomerOptional.get();

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setAge(updatedCustomer.getAge());
        existingCustomer.setWallet_balance(updatedCustomer.getWallet_balance());
        existingCustomer.setActive(true);
       
        customerRepository.save(existingCustomer);
        log.info("Updated customer with id {}", existingCustomer.getId());
        
        return mapToCustomerResponse(existingCustomer);
        
    }


	@Override
    public boolean deleteCustomer(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            customer.setActive(false);
            customerRepository.save(customer);
            return true;
        }
        return false;
    }

	



	
}
