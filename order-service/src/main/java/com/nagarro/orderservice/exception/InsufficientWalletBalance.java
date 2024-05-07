package com.nagarro.orderservice.exception;

public class InsufficientWalletBalance extends RuntimeException {
	
	public InsufficientWalletBalance(String message) {
		super(message);
	}

}
