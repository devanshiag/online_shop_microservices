package com.nagarro.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.orderservice.model.Order;
import com.nagarro.orderservice.model.OrderStatus;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findByStatus(OrderStatus pending);

}
