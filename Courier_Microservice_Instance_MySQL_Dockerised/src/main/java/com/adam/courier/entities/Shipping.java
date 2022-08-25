package com.adam.courier.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;

import com.adam.courier.dto.ShippingDTO;

@Entity
public class Shipping {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@Positive(message="Shipping Price with tax must be greater than 0")
	private double priceTaxIncluded;
	@Positive(message="Shipping Price WITHOUT tax must be greater than 0")
	private double priceTaxExcluded;
	@Positive(message="Shipping Weight must be greater than 0")
	private double shippingWeight;
	
	public Shipping()
	{
		
	}

	public Shipping(Long id, double priceTaxIncluded, double priceTaxExcluded, double shippingWeight) {
		super();
		this.id = id;
		this.priceTaxIncluded = priceTaxIncluded;
		this.priceTaxExcluded = priceTaxExcluded;
		this.shippingWeight = shippingWeight;
	}
	
	public Shipping(ShippingDTO shipping)
	{
		this.priceTaxIncluded = shipping.getPriceTaxIncluded();
		this.priceTaxExcluded = shipping.getPriceTaxExcluded();
		this.shippingWeight = shipping.getShippingWeight();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPriceTaxIncluded() {
		return priceTaxIncluded;
	}

	public void setPriceTaxIncluded(double priceTaxIncluded) {
		this.priceTaxIncluded = priceTaxIncluded;
	}

	public double getPriceTaxExcluded() {
		return priceTaxExcluded;
	}

	public void setPriceTaxExcluded(double priceTaxExcluded) {
		this.priceTaxExcluded = priceTaxExcluded;
	}

	public double getShippingWeight() {
		return shippingWeight;
	}

	public void setShippingWeight(double shippingWeight) {
		this.shippingWeight = shippingWeight;
	}

	@Override
	public String toString() {
		return "Shipping [id=" + id + ", priceTaxIncluded=" + priceTaxIncluded + ", priceTaxExcluded="
				+ priceTaxExcluded + ", shippingWeight=" + shippingWeight + "]";
	}
	
}
