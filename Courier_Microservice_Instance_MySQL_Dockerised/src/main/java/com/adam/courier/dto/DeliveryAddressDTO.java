package com.adam.courier.dto;

import javax.validation.constraints.NotNull;

import com.adam.courier.entities.DeliveryAddress;

import io.swagger.annotations.ApiModel;

@ApiModel(description="DeliveryAddressDTO Class")
public class DeliveryAddressDTO {
	
	private Long id;
	
	@NotNull
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String county;
	private String postcode;
	private String phoneNo;
	
	public DeliveryAddressDTO()
	{
		
	}
	
	public DeliveryAddressDTO(Long id, String firstName, String lastName, String address1, String address2, String city,
			String county, String postcode, String phoneNo) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.county = county;
		this.postcode = postcode;
		this.phoneNo = phoneNo;
	}
	
	public DeliveryAddressDTO(DeliveryAddress address)
	{
		this.id = address.getId();
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

	public void setId(Long id) {
		this.id = id;
	}

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
		return "DeliveryAddress [firstName=" + firstName + ", lastName=" + lastName + ", address1=" + address1
				+ ", address2=" + address2 + ", city=" + city + ", county=" + county + ", postcode=" + postcode
				+ ", phoneNo=" + phoneNo + "]";
	}

}
