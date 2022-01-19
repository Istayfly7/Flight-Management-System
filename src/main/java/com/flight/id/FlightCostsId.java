package com.flight.id;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class FlightCostsId implements Serializable{
	
	private int flight_costs_number;
	
	private int aircraft_type_code;
	
	//private Date valid_from_date;
	
	public FlightCostsId() {}
	
	public FlightCostsId(int flight_costs_number, int aircraft_type_code/*, Date valid_from_date*/) {
		this.flight_costs_number = flight_costs_number;
		this.aircraft_type_code = aircraft_type_code;
		//this.valid_from_date = valid_from_date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + aircraft_type_code;
		result = prime * result + flight_costs_number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightCostsId other = (FlightCostsId) obj;
		if (aircraft_type_code != other.aircraft_type_code)
			return false;
		if (flight_costs_number != other.flight_costs_number)
			return false;
		return true;
	}
	
}
