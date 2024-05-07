package com.nagarro.orderservice.dto;

import java.time.LocalDate;

import com.nagarro.orderservice.model.OrderStatus;

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
    private int quantity;
    private OrderStatus status;
}
