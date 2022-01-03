package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="Flight_Costs")
public abstract class FlightCosts {

	@OneToOne
	@JoinColumn(name="flight_number")
	private FlightSchedules flight_number;
	
	@OneToOne
	@JoinColumn(name="aircraft_type_code")
	private TravelClassCapacity aircraft_type_code;
	
	@OneToOne
	@JoinColumn(name="departure_date_time")
	private FlightSchedules valid_from_date;
	
	@OneToOne
	@JoinColumn(name="arrival_date_time")
	private FlightSchedules valid_to_date;
	
	@Column(nullable=false)
	private double flight_cost;

	public double calculateOneWay(FlightSchedules valid_from_date, ItineraryReservations reservation) {
		return calculate(valid_from_date, reservation);
	}
	
	/*public void calculateRoundTrip(FlightSchedules valid_from_date, FlightSchedules valid_to_date, List<ItineraryReservations> reservations) {
		return calculate(valid_from_date, reservations.get(0)) + calculate(valid_to_date, reservations.get(1)));
	}*/
	
	private double calculate(FlightSchedules flightSchedule, ItineraryReservations reservation) {
		double price = 0.0;
		
		if(reservation.getTravel_class_code() == 1) {
			price = 0.77 * calculateDistance(flightSchedule.getOrigin_airport_code().getAirport_location(), flightSchedule.getDestination_airport_code().getAirport_location()); 
		}
		else{
			price = 0.24 * calculateDistance(flightSchedule.getOrigin_airport_code().getAirport_location(), flightSchedule.getDestination_airport_code().getAirport_location()); 
		}
		
		return price;
	}
	
	private int calculateDistance(String originLocation, String destinationLocation) {
		return 1;
	}

	
	public void setFlight_cost(double flight_cost) {
		this.flight_cost = flight_cost;
	}
	
	public double getFlight_cost() {
		return this.flight_cost;
	}
	
	public FlightSchedules getFlight() {
		return this.flight_number;
	}

}
