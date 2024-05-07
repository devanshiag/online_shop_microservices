package com.nagarro.orderservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nagarro.orderservice.dto.CustomerResponse;
import com.nagarro.orderservice.dto.OrderRequest;
import com.nagarro.orderservice.dto.OrderResponse;
import com.nagarro.orderservice.exception.CustomerNotFoundException;
import com.nagarro.orderservice.exception.InsufficientWalletBalance;
import com.nagarro.orderservice.exception.OrderNotFoundException;
import com.nagarro.orderservice.exception.ProductNotFoundException;
import com.nagarro.orderservice.model.Order;
import com.nagarro.orderservice.model.OrderStatus;
import com.nagarro.orderservice.model.Product;
import com.nagarro.orderservice.repository.OrderRepository;
import com.nagarro.orderservice.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{
	
    private final String CUSTOMER_SERVICE_URL = "http://localhost:8585/api/v1/customers";

//	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {
		CustomerResponse customer = null;;
		
	    try {
	        customer = restTemplate.getForObject(CUSTOMER_SERVICE_URL + "/" + orderRequest.getCustomerId(), CustomerResponse.class);
	        log.info("customer found with id {}", orderRequest.getCustomerId());
	    } catch (HttpClientErrorException.NotFound e) {
	        // Customer not found, create a new customer
	    	throw new CustomerNotFoundException("No customer found with id " + orderRequest.getCustomerId());
	    }catch (Exception e) {
			e.printStackTrace();
		}
	    
    	log.info(orderRequest.toString());

    	Optional<Product> product = productRepository.findById(orderRequest.getProductId());
    	
    	//check if product exists
    	if(!product.isPresent()) 
            throw new ProductNotFoundException("Product not found with ID: " + orderRequest.getProductId());
    	
    	Order order = new Order();

    	order.setProductId(orderRequest.getProductId());
    	order.setQuantity(orderRequest.getQuantity());
    	order.setCustomerId(orderRequest.getCustomerId());
    	order.setOrderDate(LocalDate.now());
    	order.setStatus(OrderStatus.PENDING);    	

    	log.info("the order received in request is {}", order.toString());
    	
    	
	    if(product.get().getCost() * order.getQuantity() <= customer.getWallet_balance()) {
         	  order.setStatus(OrderStatus.CONFIRMED);
         	  
         	  //deduct customer's balance
         	  /*
         	   * 
         	   * 
         	   * 
         	   * 
         	   * 
         	   * 
         	   */
          }
        else {
        	  throw new InsufficientWalletBalance("Insufficient balance in wallet to purchase product with id "+order.getProductId());
        }
		
	    
	    orderRepository.save(order);
	    log.info("Order saved "+ order.toString());
		return mapToOrderResponse(order);
	}
	
	private OrderResponse mapToOrderResponse(Order order) {
		return OrderResponse.builder()
				.id(order.getId())
				.customerId(order.getCustomerId())
				.orderDate(order.getOrderDate())
				.productId(order.getProductId())
				.quantity(order.getQuantity())
				.status(order.getStatus())
				.build();
	}


	@Override
	public Optional<OrderResponse> cancelOrder(Long id) {
		Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
        else {
        	throw new OrderNotFoundException("No order present with id "+id);
        }
		return optionalOrder.map(o->mapToOrderResponse(o));
		
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		List<Order> orders = orderRepository.findAll();
		return orders.stream().map(o->mapToOrderResponse(o)).toList();
		
	}

	@Override
	public Optional<OrderResponse> getOrderById(Long id) {
		Optional<Order> order = orderRepository.findById(id);
		return order.map(o->mapToOrderResponse(o));
		
	}

	/*
	 * checks if the customer has enough wallet balance to buy product in their order and then confirms order
	 */
//	@Override
//	public OrderResponse placeOrder(Long id) throws ProductNotFoundException{
//		Optional<Order> optionalorder =  orderRepository.findById(id);
//		if(optionalorder.isPresent()) {
//			Order order = optionalorder.get();
//			
//		
//			Optional<Product> optionalProduct = productRepository.findById(order.getProductId());
//			int quantity = order.getQuantity();
//			
//	        if (!optionalProduct.isPresent()) {
//	            throw new ProductNotFoundException("Product not found with ID: " + order.getProductId());
//	        }	        
//	        
//	    	Customer customer =  restTemplate.getForObject(CUSTOMER_SERVICE_URL + "/" + order.getCustomerId(), Customer.class);
//
//	        
//	        if(optionalProduct.get().getCost() * quantity <= customer.getWallet_balance()) {
//	        	order.setStatus(OrderStatus.CONFIRMED);
//				return orderRepository.save(order);
//	        }
//	        else {
//	        	throw new InsufficientWalletBalance("Insufficient balance in wallet to purchase product with id "+order.getProductId());
//	        }
//		}
//		return null;
//		
//	}


}
