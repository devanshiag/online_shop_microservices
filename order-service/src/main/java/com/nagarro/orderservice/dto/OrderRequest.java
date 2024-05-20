package com.nagarro.orderservice.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest{
	
	@NotNull(message = "Customer id is required")
	@Column(name = "customer_id")
    private Long customerId;

	@NotNull(message = "Product id is required")
    @Column(name = "product_id")
    private Long productId;
    
	@NotNull(message = "Product quantity is required")
    @Column(name = "product_quantity")
	@Min(value = 1, message = "At least 1 quantity is required")
    private Integer quantity;

}




//public class OrderRequest {
//
//	private Order order;
//  private Customer customer;
//  
//  
//	public OrderRequest() {
//		super();
//	}
//	public OrderRequest(Order order, Customer customer) {
//		super();
//		this.order = order;
//		this.customer = customer;
//	}
//	public Order getOrder() {
//		return order;
//	}
//	public void setOrder(Order order) {
//		this.order = order;
//	}
//	public Customer getCustomer() {
//		return customer;
//	}
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
//}