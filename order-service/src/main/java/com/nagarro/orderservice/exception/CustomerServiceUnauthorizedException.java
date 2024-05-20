package com.nagarro.orderservice.exception;

public class CustomerServiceUnauthorizedException extends RuntimeException {

	public CustomerServiceUnauthorizedException(String message) {
		super(message);
	}
}
