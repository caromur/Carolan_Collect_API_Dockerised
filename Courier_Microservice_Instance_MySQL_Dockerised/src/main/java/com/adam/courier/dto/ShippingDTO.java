package com.adam.courier.dto;

import com.adam.courier.entities.Shipping;

import io.swagger.annotations.ApiModel;

@ApiModel(description="ShippingDTO Class")
public class ShippingDTO {
	
	private double priceTaxIncluded;
	private double priceTaxExcluded;
	private double shippingWeight;
	
	public ShippingDTO()
	{
		
	}

	public ShippingDTO(double priceTaxIncluded, double priceTaxExcluded, double shippingWeight) {
		super();
		this.priceTaxIncluded = priceTaxIncluded;
		this.priceTaxExcluded = priceTaxExcluded;
		this.shippingWeight = shippingWeight;
	}
	
	public ShippingDTO(Shipping shipping)
	{
		this.priceTaxIncluded = shipping.getPriceTaxIncluded();
		this.priceTaxExcluded = shipping.getPriceTaxExcluded();
		this.shippingWeight = shipping.getShippingWeight();
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
		return "Shipping [priceTaxIncluded=" + priceTaxIncluded + ", priceTaxExcluded=" + priceTaxExcluded
				+ ", shippingWeight=" + shippingWeight + "]";
	}

}
