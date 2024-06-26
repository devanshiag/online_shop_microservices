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

	private Long id;
	private String name;
	private Integer age;
	private Double wallet_balance;
	private Boolean active;
}
