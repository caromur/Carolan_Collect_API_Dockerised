package com.adam.courier.dao;

import org.springframework.data.repository.CrudRepository;

import com.adam.courier.entities.Shipping;

public interface ShippingRepository extends CrudRepository<Shipping, Long>{

}
