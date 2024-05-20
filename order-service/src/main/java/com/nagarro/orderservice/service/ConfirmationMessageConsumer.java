package com.nagarro.orderservice.service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.nagarro.orderservice.dto.ConfirmationDTO;
import com.nagarro.orderservice.model.Order;
import com.nagarro.orderservice.model.OrderStatus;
import com.nagarro.orderservice.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConfirmationMessageConsumer {


    private final OrderService orderService;
    private final OrderRepository orderRepository;
    
    private final int confirmationTimeoutSeconds = 5; // Timeout in seconds



    public ConfirmationMessageConsumer(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${nagarro.rabbitmq.confirmationQueue}")
    public void receiveConfirmation(ConfirmationDTO confirmationDTO) {
        log.info("Received confirmation for order ID: {}", confirmationDTO.toString());
        
        if(confirmationDTO.getStatus()) {
        	orderService.confirmOrder(confirmationDTO.getOrderId());
        }
        else {
        	orderService.cancelOrder(confirmationDTO.getOrderId());
        }
	    
    }
    
    @Scheduled(cron = "*/30 * * * * *")
    public void checkPendingOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        log.info("checking pending orders");
        for (Order order : pendingOrders) {
            // Check if order confirmation was not received within the timeout period
            if (Duration.between(order.getOrderDate().atStartOfDay(), LocalDateTime.now()).getSeconds() >= confirmationTimeoutSeconds) {
                log.warn("Confirmation not received for order ID {}. Cancelling the order.", order.getId());
                orderService.cancelOrder(order.getId());
            }
        }
    }
}