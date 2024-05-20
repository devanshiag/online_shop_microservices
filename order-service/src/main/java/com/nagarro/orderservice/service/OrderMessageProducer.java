package com.nagarro.orderservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nagarro.orderservice.dto.CustomerRequest;
import com.nagarro.orderservice.dto.OrderMessageDTO;
import com.nagarro.orderservice.dto.OrderResponse;
import com.nagarro.orderservice.model.Order;
import com.nagarro.orderservice.model.OrderStatus;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class OrderMessageProducer {
	
	@Value("${nagarro.rabbitmq.orderExchange}")
	String orderExchange;

	@Value("${nagarro.rabbitmq.orderRoutingKey}")
	private String orderRoutingKey;

	private final RabbitTemplate rabbitTemplate;
	
	public OrderMessageProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void sendMessage(Order order) {
		OrderMessageDTO orderMessage = new OrderMessageDTO();
		
		orderMessage.setId(order.getId());
		orderMessage.setCustomerId(order.getCustomerId());
		orderMessage.setOrderDate(order.getOrderDate());
		orderMessage.setProductId(order.getProductId());
		orderMessage.setQuantity(order.getQuantity());
		orderMessage.setTotalCost(order.getTotalCost());
		log.info(orderMessage.toString());

		rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, orderMessage);
	}
}
