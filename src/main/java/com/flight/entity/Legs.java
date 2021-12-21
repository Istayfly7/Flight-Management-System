package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.model.FlightCosts;

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
	private String actual_depature_time; //sql time object
	
	@Column(nullable=false)
	private String actual_arrival_time; //sql time object
}
