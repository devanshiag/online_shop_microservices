package com.nagarro.customerservice.dto;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private Long productId;
    private Integer quantity;
}
