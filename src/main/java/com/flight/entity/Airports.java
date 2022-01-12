package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Airports")
public class Airports {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int airport_code;
	
	@Column(nullable=false)
	private String airport_name;
	
	@Column(nullable=false)
	private String airport_location;
	
	private String other_details;

	public int getAirport_code() {
		return this.airport_code;
	}
	
	public String getAirport_name() {
		return this.airport_name;
	}

	public String getAirport_location() {
		return airport_location;
	}
	
	public String getOther_details() {
		return other_details;
	}

	public void setAirport_name(String airport_name) {
		this.airport_name = airport_name;
	}

	public void setAirport_location(String airport_location) {
		this.airport_location = airport_location;
	}

	public void setOther_details(String other_details) {
		this.other_details = other_details;
	}
	
	
	
}
