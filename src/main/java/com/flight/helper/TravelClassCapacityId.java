package com.flight.helper;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TravelClassCapacityId implements Serializable {
	
	private int aircraft_type_code;
	
	private int travel_class_code;
	
	public TravelClassCapacityId() {}
	
	public TravelClassCapacityId(int aircraft_type_code, int travel_class_code) {
		this.aircraft_type_code = aircraft_type_code;
		this.travel_class_code = travel_class_code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + aircraft_type_code;
		result = prime * result + travel_class_code;
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
		TravelClassCapacityId other = (TravelClassCapacityId) obj;
		if (aircraft_type_code != other.aircraft_type_code)
			return false;
		if (travel_class_code != other.travel_class_code)
			return false;
		return true;
	}

}
