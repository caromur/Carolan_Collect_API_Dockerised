package com.adam.courier.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.adam.courier.dto.LabelDTO;
import com.adam.courier.dto.ProductDTO;

/**
 * Label Object consisting of Address, List of Products and Shipping objects
 * @author adamc
 *
 */

@Entity
public class Label {
	
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	//@GeneratedValue
	@Positive(message="id must be positive")
	Long id;
	
	@OneToOne(targetEntity=DeliveryAddress.class, cascade=CascadeType.ALL)
	@JoinColumn(name="address_id", referencedColumnName="id")
	@NotNull(message="Delivery address cannot be null")
	private DeliveryAddress address;
	
	@OneToMany(targetEntity=Product.class, cascade=CascadeType.ALL)
	@JoinColumn(name="label_id", referencedColumnName="id")
	private List<Product> products;
	
	@OneToOne(targetEntity=Shipping.class, cascade=CascadeType.ALL)
	@JoinColumn(name="shipping_id", referencedColumnName="id")
	@NotNull(message="Shipping details cannot be null")
	private Shipping shipping;
	
	public Label() {}
	
	public Label(Long id, DeliveryAddress address, List<Product> products, Shipping shipping) {
		super();
		this.id = id;
		this.address = address;
		this.products = products;
		this.shipping = shipping;
	}
	
	public Label(LabelDTO label)
	{
		this.id = label.getId();
		this.address = new DeliveryAddress(label.getAddress());
		List<ProductDTO> prodsDTO = label.getProducts();
		List<Product> prods = new ArrayList<>();
		for(ProductDTO pr: prodsDTO)
		{
			Product p = new Product(pr);
			prods.add(p);
		}
		this.products = prods;
		this.shipping = new Shipping(label.getShipping());
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DeliveryAddress getAddress() {
		return address;
	}

	public void setAddress(DeliveryAddress address) {
		this.address = address;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	@Override
	public String toString() {
		return "Label [id=" + id + ", address=" + address + ", products=" + products + ", shipping=" + shipping + "]";
	}
}
