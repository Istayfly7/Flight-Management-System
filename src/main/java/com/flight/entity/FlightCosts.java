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
@Table(name="Flight_Costs")
public class FlightCosts {

	@OneToOne
	@JoinColumn(name="flight_number")
	private FlightSchedules flight_number;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int aircraft_type_code;
	
	@OneToOne
	@JoinColumn(name="departure_date_time")
	private FlightSchedules valid_from_date;
	
	@OneToOne
	@JoinColumn(name="arrival_date_time")
	private FlightSchedules valid_to_date;
	
	@Column(nullable=false)
	private int flight_cost;
	
	{
		this.flight_cost = calculate(this.valid_from_date, this.valid_to_date);
	}

	private int calculate(FlightSchedules valid_from_date, FlightSchedules valid_to_date) {
		return calculate(valid_from_date) + calculate(valid_to_date);
	}
	
	private int calculate(FlightSchedules flightSchedule) {
		return 0;
	}

}
