package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Travel_Class_Capacity")
public class TravelClassCapacity {
	
	@OneToOne
	@JoinColumn(name="aircraft_type_code")
	private FlightCosts aircraft_type_code;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int travel_class_code;
	
	@Column(nullable=false)
	private int seat_capcity = 30;
	
	public void reserveSeat() {
		reserveSeat(1);
	}
	
	public void reserveSeat(int num) {
		this.seat_capcity -= num;
	}
	
	public void addASeat() {
		addSeats(1);
	}
	
	public void addSeats(int num) {
		this.seat_capcity += num;
	}
}
