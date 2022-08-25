package com.adam.courier.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adam.courier.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

	
}
