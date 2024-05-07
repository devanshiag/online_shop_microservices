package com.nagarro.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

	private long id;
	private String name;
	private int age;
	private double wallet_balance;
	private boolean active;
}
