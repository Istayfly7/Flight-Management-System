package com.flight.entity;

import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Legs")
public class Legs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leg_id;
	
	@ManyToOne
	@JoinColumn(name="flight_number")
	private FlightSchedules flight_number;
	
	@Column(nullable=false)
	private String origin_airport;
	
	@Column(nullable=false)
	private String destination_airport;
	
	@Column(nullable=false)
	private Time actual_departure_time;
	
	@Column(nullable=false)
	private Time actual_arrival_time;
	
	public Legs() {}
	
	public Legs(FlightSchedules flight_number) {
		this.flight_number = flight_number;
		this.origin_airport = flight_number.getOrigin_airport_code().getAirport_name();
		this.destination_airport = flight_number.getDestination_airport_code().getAirport_name();
		this.actual_departure_time = Time.valueOf(flight_number.getDeparture_date_time().toLocalDateTime().toLocalTime());
		this.actual_arrival_time = Time.valueOf(flight_number.getArrival_date_time().toLocalDateTime().toLocalTime());
	}
	
	
	public Time getActual_departure_time() {
		return actual_departure_time;
	}

	public void setFlight_number(FlightSchedules flight_number) {
		this.flight_number = flight_number;
	}

	public Time getActual_arrival_time() {
		return actual_arrival_time;
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

	public void setOrigin_airport(String origin_airport) {
		this.origin_airport = origin_airport;
	}

	public void setDestination_airport(String destination_airport) {
		this.destination_airport = destination_airport;
	}

	public void setActual_departure_time(Time actual_departure_time) {
		this.actual_departure_time = actual_departure_time;
	}

	public void setActual_arrival_time(Time actual_arrival_time) {
		this.actual_arrival_time = actual_arrival_time;
	}

	public int getLeg_id() {
		return leg_id;
	}	
	
}
