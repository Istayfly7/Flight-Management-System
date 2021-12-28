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
@Table(name="Legs")
public class Legs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leg_id;
	
	@OneToOne
	@JoinColumn(name="flight_number")
	private FlightSchedules flight_number;
	
	@Column(nullable=false)
	private String origin_airport;
	
	@Column(nullable=false)
	private String destination_airport;
	
	@Column(nullable=false)
	private int actual_departure_time; //sql time object
	
	@Column(nullable=false)
	private int actual_arrival_time; //sql time object

	public int getActual_departure_time() {
		return actual_departure_time;
	}

	public void setActual_departure_time(int actual_departure_time) {
		this.actual_departure_time = actual_departure_time;
	}

	public int getActual_arrival_time() {
		return actual_arrival_time;
	}

	public void setActual_arrival_time(int actual_arrival_time) {
		this.actual_arrival_time = actual_arrival_time;
	}
	
	public FlightSchedules getFlight_Number() {
		return this.flight_number;
	}

	public String getOrigin_airport() {
		return origin_airport;
	}

	public String getDestination_airport() {
		return destination_airport;
	}
	
}
