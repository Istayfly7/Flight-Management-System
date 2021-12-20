package com.flight.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@Entity
@DiscriminatorValue("EMPLOYEE")
public class EmployeeUser {

	@JsonIgnore
	public String getType() {
		return "EMPLOYEE";
	}
}
