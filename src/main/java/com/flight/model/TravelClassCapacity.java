package com.flight.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.entity.FlightCosts;

@Table(name="Travel_Class_Capacity")
public class TravelClassCapacity {
	
	@OneToOne
	@JoinColumn(name="aircraft_type_code")
	private FlightCosts aircraft_type_code;
	
	private int travel_class_code;
	
	@Column(nullable=false)
	private int seat_capcity;
	
	public TravelClassCapacity() {
		this.travel_class_code = 2;
		setSeat_capacity(this.travel_class_code);
	}
	
	public TravelClassCapacity(int travel_class_code) {
		this.travel_class_code = travel_class_code;
		setSeat_capacity(this.travel_class_code);
	}
	
	private void setSeat_capacity(int travel_class_code) {
		if(travel_class_code == 1) {
			seat_capcity = 10;
		}
		else {
			seat_capcity = 30;
		}
	}
	
	public void reserveSeat() {
		this.seat_capcity -= 1;
	}
}
