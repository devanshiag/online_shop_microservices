package com.nagarro.customerservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

	@NotBlank(message = "Name cannot be blank")
	@NotNull(message = "Name cannot be null")
	private String name;
	
	@NotNull(message = "Age cannot be null")
    @Positive(message = "Age should be a positive number")
	@Min(value=13, message = "Minimum age should be at least 13")
	@Max(value = 110, message = "Maximum age can be 110")
	private Integer age;
	
	@Min(value = 0, message = "Balance cannot be negative")
	private double wallet_balance;
}
