package com.nagarro.orderservice.exception;

public class ProductNotFoundException extends NotFoundException {

	public ProductNotFoundException(String message) {
        super(message);
    }
}
