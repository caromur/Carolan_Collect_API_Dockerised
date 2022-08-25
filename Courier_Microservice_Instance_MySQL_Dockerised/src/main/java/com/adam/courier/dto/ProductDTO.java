package com.adam.courier.dto;

import com.adam.courier.entities.Product;

import io.swagger.annotations.ApiModel;

@ApiModel(description="ProductDTO Class")
public class ProductDTO {
	
	private Long id;
	private String name;
	private int quantity;
	private double price;
	private double priceTaxIncluded;
	
	public ProductDTO()
	{
		
	}
	
	public ProductDTO(Long id, String name, int quantity, double price, double priceTaxIncluded) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.priceTaxIncluded = priceTaxIncluded;
	}
	
	public ProductDTO(Product product)
	{
		this.id = product.getId();
		this.name = product.getName();
		this.quantity = product.getQuantity();
		this.price = product.getPrice();
		this.priceTaxIncluded = product.getPriceTaxIncluded();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPriceTaxIncluded() {
		return priceTaxIncluded;
	}

	public void setPriceTaxIncluded(double priceTaxIncluded) {
		this.priceTaxIncluded = priceTaxIncluded;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", quantity=" + quantity + ", price=" + price + ", priceTaxIncluded="
				+ priceTaxIncluded + "]";
	}

}
