package com.nagarro.customerservice.service;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nagarro.customerservice.dto.ConfirmationDTO;
import com.nagarro.customerservice.dto.CustomerRequest;
import com.nagarro.customerservice.dto.CustomerResponse;
import com.nagarro.customerservice.dto.OrderMessageDTO;
import com.nagarro.customerservice.dto.OrderResponse;
import com.nagarro.customerservice.exception.CustomerNotFoundException;
import com.nagarro.customerservice.exception.InsufficientWalletBalance;
import com.nagarro.customerservice.model.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderMessageConsumer {
	
	@Value("${nagarro.rabbitmq.confirmationExchange}")
	String confirmationExchange;

	@Value("${nagarro.rabbitmq.confirmationRoutingKey}")
	private String confirmationRoutingKey;


    private final CustomerService customerService;
    private final RabbitTemplate rabbitTemplate;

    public OrderMessageConsumer(CustomerService customerService, RabbitTemplate rabbitTemplate) {
        this.customerService = customerService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${nagarro.rabbitmq.orderQueue}")
    public void consumeMessage(OrderMessageDTO orderMessage) {
    	
        try {
            ConfirmationDTO confirmationDTO = ConfirmationDTO.builder().orderId(orderMessage.getId()).status(false).build();

            if (orderMessage != null) {
                log.info("Message consumed in customer service");
                Optional<CustomerResponse> optionalCustomer = customerService.getCustomerById(orderMessage.getCustomerId());

                if (optionalCustomer.isPresent()) {
                    CustomerResponse customer = optionalCustomer.get();

                    CustomerRequest customerRequest = CustomerRequest.builder()
                            .name(customer.getName())
                            .age(customer.getAge())
                            .wallet_balance(customer.getWallet_balance() - orderMessage.getTotalCost())
                            .build();
//                    try {
//            			Thread.sleep(20000);
//            		} catch (InterruptedException e) {
//            			e.printStackTrace();
//            		}

                    // Attempt to update the customer
                    try {
                        // Update the customer
                        CustomerResponse updatedCustomer = customerService.updateCustomer(orderMessage.getCustomerId(), customerRequest);
                        
                        // If the update was successful, set status to true
                        confirmationDTO.setStatus(true);
                        
                        log.info("Customer updated successfully");
                        
                    } catch (CustomerNotFoundException e) {
                    	
                        // Customer not found, logging a warning and keep status as false
                        log.warn("Customer not found for ID {}", orderMessage.getCustomerId());
                    }
                } else {
                    log.warn("Customer not found for ID {}", orderMessage.getCustomerId());
                }
                
                // Sending confirmation message with the status
                log.info("Sending confirmation message with status: {}", confirmationDTO.getStatus());
                rabbitTemplate.convertAndSend(confirmationExchange, confirmationRoutingKey, confirmationDTO);

            } 
        } catch (Exception ex) {
            log.error("Error processing order message: {}", ex.getMessage());
        }
    }
}
