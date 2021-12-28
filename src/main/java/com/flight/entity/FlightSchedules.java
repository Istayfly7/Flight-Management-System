package com.flight.entity;

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

@Entity
@Table(name="Flight_Schedules")
public class FlightSchedules {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int flight_number;
	
	/*@OneToOne
	@JoinColumn(name="aircraft_type_code")
	private FlightCosts airline_code;*/
	
	@OneToMany
	@JoinColumn(name="aircraft_type_code")
	private List<TravelClassCapacity> usual_aircraft_type_code;
	
	@OneToOne
	@JoinColumn(name="airport_code", insertable=false, updatable=false)
	private Airports origin_airport_code;
	
	@OneToOne
	@JoinColumn(name="airport_code", insertable=false, updatable=false)
	private Airports destination_airport_code;
	
	@Column(nullable=false)
	private int departure_date_time;
	
	@Column(nullable=false)
	private int arrival_date_time;

	public Airports getOrigin_airport_code() {
		return origin_airport_code;
	}

	public Airports getDestination_airport_code() {
		return destination_airport_code;
	}
	
	public int getFlight_number() {
		return this.flight_number;
	}

	public int getDeparture_date_time() {
		return departure_date_time;
	}

	public int getArrival_date_time() {
		return arrival_date_time;
	}
	
}
