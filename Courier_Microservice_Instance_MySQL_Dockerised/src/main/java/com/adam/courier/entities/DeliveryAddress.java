package com.adam.courier.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.adam.courier.dto.DeliveryAddressDTO;

@Entity
public class DeliveryAddress {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//@NotNull(message="id cannot be null")
	@Positive(message="id must be positive")
	private Long id;
	
	@NotNull(message = "First name cannot be null")
	@NotEmpty(message = "First name cannot be empty")
	private String firstName;
	@NotNull(message = "Surname cannot be null")
	@NotEmpty(message = "Surname cannot be empty")
	private String lastName;
	@NotNull(message = "Address 1 cannot be null")
	@NotEmpty(message = "Address 1 cannot be empty")
	private String address1;
	private String address2;
	@NotNull(message = "City cannot be null")
	@NotEmpty(message = "City cannot be empty")
	private String city;
	@NotNull(message = "County cannot be null")
	@NotEmpty(message = "County cannot be empty")
	private String county;
	@NotNull(message = "Postcode cannot be null")
	@NotEmpty(message = "Postcode cannot be empty")
	private String postcode;
	@NotNull(message = "Phone number cannot be null")
	@NotEmpty(message = "Phone number cannot be empty")
	private String phoneNo;
	
	public DeliveryAddress()
	{
		
	}
	
	public DeliveryAddress(/*Long id,*/ String firstName, String lastName, String address1, String address2, String city,
			String county, String postcode, String phoneNo) {
		super();
		//this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.county = county;
		this.postcode = postcode;
		this.phoneNo = phoneNo;
	}
	
	public DeliveryAddress(DeliveryAddressDTO address)
	{
		//this.id = address.getId();
		this.firstName = address.getFirstName();
		this.lastName = address.getLastName();
		this.address1 = address.getAddress1();
		this.address2 = address.getAddress2();
		this.city = address.getCity();
		this.county = address.getCounty();
		this.postcode = address.getPostcode();
		this.phoneNo = address.getPhoneNo();
	}
	
	public Long getId() {
		return id;
	}

//	public void setId(Long id) {
//		this.id = id;
//	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Override
	public String toString() {
		return "DeliveryAddress [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", address1="
				+ address1 + ", address2=" + address2 + ", city=" + city + ", county=" + county + ", postcode="
				+ postcode + ", phoneNo=" + phoneNo + "]";
	}

}
