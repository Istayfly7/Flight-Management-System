package com.flight.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Flight_Schedules")
public class FlightSchedules {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int flight_number;
	
	@OneToOne(mappedBy="valid_to_date")
	@JoinColumn(name="flight_costs")
	private FlightCosts airline_code;
	
	@ManyToOne
	private TravelClassCapacity usual_aircraft_type_code;
	
	@ManyToOne
	@JoinColumn(insertable=false, updatable=false)
	private Airports origin_airport_code;
	
	@ManyToOne
	@JoinColumn(insertable=false, updatable=false)
	private Airports destination_airport_code;
	
	@Column(nullable=false)
	private Timestamp departure_date_time;
	
	@Column(nullable=false)
	private Timestamp arrival_date_time;

	public Airports getOrigin_airport_code() {
		return origin_airport_code;
	}

	public Airports getDestination_airport_code() {
		return destination_airport_code;
	}
	
	public int getFlight_number() {
		return this.flight_number;
	}

	public Timestamp getDeparture_date_time() {
		return departure_date_time;
	}

	public Timestamp getArrival_date_time() {
		return arrival_date_time;
	}
	
	//special ocassions
	public void setFlight_number(int flight_number) {
		this.flight_number = flight_number;
	}

	public void setOrigin_airport_code(Airports origin_airport_code) {
		this.origin_airport_code = origin_airport_code;
	}

	public void setDestination_airport_code(Airports destination_airport_code) {
		this.destination_airport_code = destination_airport_code;
	}

	public void setDeparture_date_time(Timestamp departure_date_time) {
		this.departure_date_time = departure_date_time;
	}

	public void setArrival_date_time(Timestamp arrival_date_time) {
		this.arrival_date_time = arrival_date_time;
	}

	public void setUsual_aircraft_type_code(TravelClassCapacity usual_aircraft_type_code) {
		this.usual_aircraft_type_code = usual_aircraft_type_code;
	}
	
	public TravelClassCapacity getUsual_aircraft_type_code() {
		return usual_aircraft_type_code;
	}

	public FlightCosts getAirline_code() {
		return airline_code;
	}

	public void setAirline_code(FlightCosts airline_code) {
		this.airline_code = airline_code;
	}

	
	/*public void calculate() {
		//this.setFlight_cost(0.24 * calculateDistance(this.origin_airport_code.getAirport_location(), this.destination_airport_code.getAirport_location()));
	}
	
	private int calculateDistance(String originLocation, String destinationLocation) {//find actual distance
		return 1;
	}*/
	
}
