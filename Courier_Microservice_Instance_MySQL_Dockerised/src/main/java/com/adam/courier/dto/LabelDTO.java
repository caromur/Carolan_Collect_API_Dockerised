package com.adam.courier.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.adam.courier.entities.Label;
import com.adam.courier.entities.Product;

import io.swagger.annotations.ApiModel;

/**
 * Label Object consisting of Address, List of Products and Shipping objects
 * @author adamc
 *
 */
@ApiModel(description="LabelDTO Class")
public class LabelDTO {
	
	private Long id;
	
	@NotNull(message="Delivery address cannot be null")
	private DeliveryAddressDTO address;
	private List<ProductDTO> products;
	private ShippingDTO shipping;
	
	public LabelDTO() {}
	
	public LabelDTO(Long id, DeliveryAddressDTO address, List<ProductDTO> products, ShippingDTO shipping) {
		super();
		this.id = id;
		this.address = address;
		this.products = products;
		this.shipping = shipping;
	}
	
	public LabelDTO(Label label)
	{
		this.id = label.getId();
		this.address = new DeliveryAddressDTO(label.getAddress());
		List<Product> originalProducts = label.getProducts();
		List<ProductDTO> convertedProducts = new ArrayList<>();
		for(Product p: originalProducts)
		{
			convertedProducts.add(new ProductDTO(p));
		}
		this.products = convertedProducts;
		this.shipping = new ShippingDTO(label.getShipping());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DeliveryAddressDTO getAddress() {
		return address;
	}

	public void setAddress(DeliveryAddressDTO address) {
		this.address = address;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}

	public ShippingDTO getShipping() {
		return shipping;
	}

	public void setShipping(ShippingDTO shipping) {
		this.shipping = shipping;
	}

	@Override
	public String toString() {
		return "LabelDTO [id=" + id + ", address=" + address + ", products=" + products + ", shipping=" + shipping
				+ "]";
	}

}
