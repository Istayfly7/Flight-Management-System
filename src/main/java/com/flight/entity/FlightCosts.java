package com.flight.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	private int flight_number;
	
	//@OneToOne
	//@JoinColumn(name="aircraft_type_code")
	@Id
	private int aircraft_type_code;
	
	//@OneToOne
	//@JoinColumn(name="departure_date_time")
	@Id
	private Date valid_from_date;
	
	@OneToOne
	@JoinColumn(name="arrival_date_time")
	private FlightSchedules valid_to_date;
	
	@Column(nullable=false)
	private double flight_cost;

	public FlightCosts() {}
	
	public FlightCosts(int flight_number, int aircraft_type_code, Date valid_from_date) {
		this.flight_number = flight_number;
		this.aircraft_type_code = aircraft_type_code;
		this.valid_from_date = valid_from_date;
	}
	
	
	public void setFlight_cost(double cost) {
		this.flight_cost = cost;
	}

	public double getFlight_cost() {
		return this.flight_cost;
	}
	
	public int getFlight() {
		return this.flight_number;
	}

}
