package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Passengers")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
/*@JsonSubTypes({@JsonSubTypes.Type(value = CustomerUser.class, name = "CUSTOMER"),
        @JsonSubTypes.Type(value = EmployeeUser.class, name = "EMPLOYEE")
})*/
public abstract class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int passenger_id;
	
	@Column(nullable=false)
	private String first_name;
	
	private String second_name;
	
	@Column(nullable=false)
	private String last_name;
	
	@Column(nullable=true)
	private int phone_number = 000-000-0000; //int range (-2,147,483,648 to 2,147,483,647)
	
	@Column(nullable=false)
	private String email_address;
	
	@Column(nullable=false)
	private String address_lines;
	
	@Column(nullable=false)
	private String city;
	
	@Column(nullable=false)
	private String state_province_county;
	
	@Column(nullable=false)
	private String country;
	
	private String other_passenger_details;
	
	/*@Column(insertable=false, updatable=false)
	private String type;*/
	
	public abstract String getType();
	
	public int getPassengerId() {
		return this.passenger_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getSecond_name() {
		return second_name;
	}

	public void setSecond_name(String second_name) {
		this.second_name = second_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public int getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(int phone_number) {
		this.phone_number = phone_number;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getAddress_lines() {
		return address_lines;
	}

	public void setAddress_lines(String address_lines) {
		this.address_lines = address_lines;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState_province_county() {
		return state_province_county;
	}

	public void setState_province_county(String state_province_county) {
		this.state_province_county = state_province_county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOther_passenger_details() {
		return other_passenger_details;
	}

	public void setOther_passenger_details(String other_passenger_details) {
		this.other_passenger_details = other_passenger_details;
	}

}
