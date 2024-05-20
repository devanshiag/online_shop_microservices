package com.nagarro.orderservice.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.sql.results.graph.entity.internal.BatchEntityInsideEmbeddableSelectFetchInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.nagarro.orderservice.dto.OrderRequest;
import com.nagarro.orderservice.dto.OrderResponse;
import com.nagarro.orderservice.exception.OrderNotFoundException;
import com.nagarro.orderservice.model.Order;
import com.nagarro.orderservice.service.OrderService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
    private final String CUSTOMER_SERVICE_URL = "http://localhost:8585/api/v1/customers";

	@Autowired
    private OrderService orderService;
	
	@Autowired
	private RestTemplate restTemplate;
	

	@PreAuthorize("hasRole('client_user')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
    	
    	logger.info("controller: inside create");
  	
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    
    @PreAuthorize("hasRole('client_admin')")
    @GetMapping
    public ResponseEntity<Object> getAllOrders() {
            List<OrderResponse> orders = orderService.getAllOrders();
            if (orders.isEmpty()) {
            	logger.info("No orders found");
                throw new OrderNotFoundException("No orders exist.");
            }

            logger.info("Successfully fetched all orders");
            return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
        Optional<OrderResponse> searchedOrder = orderService.getOrderById(id);
        if (!searchedOrder.isPresent()) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        logger.info("Order with ID {} fetched successfully", id);
        return ResponseEntity.status(HttpStatus.OK).body(searchedOrder.get());
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> cancelOrder(@PathVariable Long id) {
        Optional<OrderResponse> cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(cancelledOrder);
    }
}
