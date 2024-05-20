package com.nagarro.customerservice.config;

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
	
	@Value("${nagarro.rabbitmq.confirmationQueue}")
	String confirmationQueue;
	
	@Value("${nagarro.rabbitmq.confirmationExchange}")
	String confirmationExchange;

	@Value("${nagarro.rabbitmq.confirmationRoutingKey}")
	private String confirmationRoutingKey;

	@Bean
	public Queue orderQueue() {
		return new Queue(orderQueue);
	}

	@Bean
	public Queue confirmationQueue() {
		return new Queue(confirmationQueue);
	}
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(confirmationExchange);
	}

	@Bean
	Binding binding(Queue confirmationQueue, DirectExchange confirmationExchange) {
		return BindingBuilder.bind(confirmationQueue).to(confirmationExchange).with(confirmationRoutingKey);
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
