package com.flight.entity;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@DiscriminatorValue("CUSTOMER")
public class CustomerUser extends User{
	
	@JsonIgnore
	public String getType() {
		return "CUSTOMER";
	}
}
