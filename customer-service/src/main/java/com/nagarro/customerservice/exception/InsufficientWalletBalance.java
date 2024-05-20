package com.nagarro.customerservice.exception;

public class InsufficientWalletBalance extends RuntimeException {
	
	public InsufficientWalletBalance(String message) {
		super(message);
	}

}
