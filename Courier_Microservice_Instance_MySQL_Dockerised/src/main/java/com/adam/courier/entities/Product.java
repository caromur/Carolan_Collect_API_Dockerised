package com.adam.courier.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.adam.courier.dto.ProductDTO;

@Entity
@Table(name="Products")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@NotEmpty(message="Product name must not be empty")
	private String name;
	@Positive(message="Product quantity must be greater than 0")
	private int quantity;
	@PositiveOrZero(message="Product price must be greater than or equal to 0")
	private double price;
	@PositiveOrZero(message="Product price with tax must be greater than or equal to 0")
	private double priceTaxIncluded;
	
//	@ManyToOne
//	private Label label;
		
	public Product()
	{
		
	}
	
	public Product(ProductDTO product)
	{
		//this.id = product.getId();
		this.name = product.getName();
		this.quantity = product.getQuantity();
		this.price = product.getPrice();
		this.priceTaxIncluded = product.getPriceTaxIncluded();
	}
	
	public Product(/*Long id,*/ String name, int quantity, double price, double priceTaxIncluded) {
		super();
		//this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.priceTaxIncluded = priceTaxIncluded;
	}

	public Long getId() {
		return id;
	}

//	public void setId(Long id) {
//		this.id = id;
//	}

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
		return "Product [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price
				+ ", priceTaxIncluded=" + priceTaxIncluded + "]";
	}

}
