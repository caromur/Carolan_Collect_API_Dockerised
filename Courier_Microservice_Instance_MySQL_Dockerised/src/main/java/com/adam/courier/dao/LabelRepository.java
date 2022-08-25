package com.adam.courier.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adam.courier.entities.Label;

public interface LabelRepository extends CrudRepository<Label, Long>{
	
	List<Label> findAllByAddressId(Long id);
	
}
