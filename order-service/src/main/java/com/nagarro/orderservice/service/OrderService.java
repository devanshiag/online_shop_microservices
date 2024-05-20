package com.nagarro.orderservice.service;

import java.util.List;
import java.util.Optional;

import com.nagarro.orderservice.dto.OrderRequest;
import com.nagarro.orderservice.dto.OrderResponse;
import com.nagarro.orderservice.exception.CustomerServiceUnauthorizedException;
import com.nagarro.orderservice.exception.ProductNotFoundException;
import com.nagarro.orderservice.model.Order;

public interface OrderService {
	
	OrderResponse createOrder(OrderRequest orderRequest) throws CustomerServiceUnauthorizedException;
    Optional<OrderResponse> cancelOrder(Long id);
    List<OrderResponse> getAllOrders();
    Optional<OrderResponse> getOrderById(Long id);
	void confirmOrder(Long orderId);

}
