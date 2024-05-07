package com.nagarro.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.orderservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
