package com.flight.id;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ItineraryLegsId implements Serializable{

	private int reservation_id;
	
	private int leg_id;
	
	public ItineraryLegsId() {}
	
	public ItineraryLegsId(int reservation_id, int leg_id) {
		this.reservation_id = reservation_id;
		this.leg_id = leg_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + leg_id;
		result = prime * result + reservation_id;
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
		ItineraryLegsId other = (ItineraryLegsId) obj;
		if (leg_id != other.leg_id)
			return false;
		if (reservation_id != other.reservation_id)
			return false;
		return true;
	}
	
	
}
