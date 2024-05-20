package com.nagarro.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	@Value("${nagarro.rabbitmq.orderQueue}")
	String orderQueue;
	
	@Value("${nagarro.rabbitmq.orderExchange}")
	String orderExchange;

	@Value("${nagarro.rabbitmq.orderRoutingKey}")
	private String orderRoutingKey;

	@Bean
	public Queue orderQueue() {
		return new Queue(orderQueue);
	}
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(orderExchange);
	}

	@Bean
	Binding binding(Queue orderQueue, DirectExchange orderExchange) {
		return BindingBuilder.bind(orderQueue).to(orderExchange).with(orderRoutingKey);
	}
	
	@Value("${nagarro.rabbitmq.confirmationQueue}")
	String confirmationQueue;
	
	@Bean
	public Queue confirmationQueue() {
		return new Queue(confirmationQueue);
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}
