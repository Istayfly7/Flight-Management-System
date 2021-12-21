package com.flight.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.entity.FlightSchedules;

@Table(name="Flight_Costs")
public class FlightCosts {

	@OneToOne
	@JoinColumn(name="flight_number")
	private FlightSchedules flight_number;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int aircraft_type_code;
	
	/*@OneToOne(mappedBy="Flight_Costs")
	private int valid_from_date;
	
	@OneToOne(mappedBy="Flight_Costs")
	private BookingAgents valid_to_date;
	
	@Column(nullable=false)
	private int flight_cost;*/
}
