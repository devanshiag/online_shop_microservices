package com.nagarro.orderservice.exception;

public class OrderNotFoundException extends NotFoundException{

	public OrderNotFoundException(String message) {
        super(message);
    }
}
