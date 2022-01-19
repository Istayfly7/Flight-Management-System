package com.flight.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.id.FlightCostsId;
import com.flight.repository.FlightSchedulesRepository;

@SuppressWarnings("serial")
@Entity
@Table(name="Flight_Costs")
@IdClass(FlightCostsId.class)
public class FlightCosts implements Serializable{

	//@OneToOne
	//@JoinColumn(name="flight_number")
	@Id
	private int flight_costs_number;
	
	//@OneToOne
	//@JoinColumn(name="aircraft_type_code")
	@Id
	private int aircraft_type_code;
	
	
	@OneToOne
	private FlightSchedules flight_schedules;
	
	private double flight_cost;

	public FlightCosts() {}
	
	public FlightCosts(int flight_costs_number, int aircraft_type_code) {
		this.flight_costs_number = flight_costs_number;
		this.aircraft_type_code = aircraft_type_code;
	}
	
	
	public void setFlight_cost(double cost) {
		this.flight_cost = cost;
	}

	public double getFlight_cost() {
		return this.flight_cost;
	}
	
	public FlightSchedules getFlight() {
		return this.flight_schedules;
	}
	
	public void setFlight(FlightSchedules flightSchedules) {
		this.flight_schedules = flightSchedules;
	}

	public int getFlight_costs_number() {
		return this.flight_costs_number;
	}

	public void setFlight_costs_number(int flight_costs_number) {
		this.flight_costs_number = flight_costs_number;
	}
	
}
