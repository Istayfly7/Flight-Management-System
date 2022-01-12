package com.flight.entity;

import java.sql.Time;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.model.ItineraryLegs;

@Entity
@Table(name="Legs")
public class Legs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leg_id;
	
	@OneToOne
	@JoinColumn(name="flight_number")
	private FlightSchedules flight_number;
	
	@OneToMany
	@JoinColumn
	private List<ItineraryLegs> itineraryLegs;
	
	@Column(nullable=false)
	private String origin_airport;
	
	@Column(nullable=false)
	private String destination_airport;
	
	@Column(nullable=false)
	private Time actual_departure_time; //sql time object
	
	@Column(nullable=false)
	private Time actual_arrival_time; //sql time object

	/*{
		this.origin_airport = flight_number.getOrigin_airport_code().getAirport_location();
		this.destination_airport = flight_number.getDestination_airport_code().getAirport_location();
		this.actual_departure_time = flight_number.getDeparture_date_time();
		this.actual_arrival_time = flight_number.getArrival_date_time();
	}*/
	
	
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
