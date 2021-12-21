package com.flight.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="Travel_Class_Capacity")
public class TravelClassCapacity {
	
	@OneToOne
	@JoinColumn(name="aircraft_type_code")
	private FlightCosts aircraft_type_code;
	
	
	/*@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int travel_class_code;*///i think referenced by airlinecode
	
	@Column(nullable=false)
	private int seat_capcity;
}
