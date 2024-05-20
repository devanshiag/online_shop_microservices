package com.nagarro.orderservice.service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.hibernate.cache.spi.support.NaturalIdNonStrictReadWriteAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nagarro.orderservice.dto.CustomerRequest;
import com.nagarro.orderservice.dto.CustomerResponse;
import com.nagarro.orderservice.dto.OrderRequest;
import com.nagarro.orderservice.dto.OrderResponse;
import com.nagarro.orderservice.exception.CustomerNotFoundException;
import com.nagarro.orderservice.exception.CustomerServiceUnauthorizedException;
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
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    private OrderMessageProducer orderMessageProducer;

	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {
		CustomerResponse customer = null;
		
	    try {
	    	ResponseEntity<CustomerResponse> responseEntity = restTemplate.exchange(
		            CUSTOMER_SERVICE_URL + "/" + orderRequest.getCustomerId(),
		            HttpMethod.GET,
		            null,
		            CustomerResponse.class
		        );
	    	customer = responseEntity.getBody();
	        log.info("customer found with id {}", orderRequest.getCustomerId());

	    } catch (HttpClientErrorException.NotFound e) {
	    	
	    	throw new CustomerNotFoundException("No customer found with id " + orderRequest.getCustomerId());
	    }catch (HttpClientErrorException.Unauthorized e) {
	       
	    	throw new CustomerServiceUnauthorizedException("Unauthorized access to the Customer service");
	    }
	    
    	log.info(orderRequest.toString());

    	Optional<Product> optionalProduct = productRepository.findById(orderRequest.getProductId());
    	
    	//check if product exists
    	if(!optionalProduct.isPresent()) 
            throw new ProductNotFoundException("Product not found with ID: " + orderRequest.getProductId());
    	
    	Order order = new Order();
    	Product product = optionalProduct.get();
    	
    	double orderTotal = product.getCost() * orderRequest.getQuantity();
    	
    	//checking the balance to start creating 
    	log.info("order total: " + orderTotal + "customer balance: "+customer.getWallet_balance());
    	
    	if(orderTotal<=customer.getWallet_balance()) {
    		order.setProductId(orderRequest.getProductId());
        	order.setQuantity(orderRequest.getQuantity());
        	order.setCustomerId(orderRequest.getCustomerId());
        	order.setOrderDate(LocalDate.now());
        	order.setTotalCost(orderTotal);
        	order.setStatus(OrderStatus.PENDING); 
    		
    	}
    	else {
			throw new InsufficientWalletBalance("Sorry, your order can't be created due to insufficient wallet balance");
		}

    	log.info("the order received in request is {}", order.toString());
    	
    	

    	if(order.getQuantity()<= product.getCount()) {
	        
    		orderRepository.save(order);
    		
    		orderMessageProducer.sendMessage(order);
    		log.info("Order message sent from order service to order queue!!");
    		
	        product.setCount(product.getCount()-order.getQuantity());
	        productRepository.save(product);
          }
        else {
        	  throw new ProductNotFoundException("Out of stock! product with id "+order.getProductId());
        }	


	    log.info("Order saved "+ order.toString());
	   
	    
		return mapToOrderResponse(order);
	}
	
	@Override
	public void confirmOrder(Long orderId) {
	    Optional<Order> optionalOrder = orderRepository.findById(orderId);
	    optionalOrder.ifPresent(order -> {
	        order.setStatus(OrderStatus.CONFIRMED);
	        log.info("Order confirmed!!!!");
	        orderRepository.save(order);
	    });
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
            
            Optional<Product> optionalProduct =  productRepository.findById(order.getProductId());
            if(optionalProduct.isPresent()) {
            	Product product = optionalProduct.get();
            	product.setCount(product.getCount() + order.getQuantity());
            	productRepository.save(product);
            }
            
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

}
