package com.flight.id;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ItineraryLegsId implements Serializable{

	private int reservation_id;
	
	private String leg_id;
	
	public ItineraryLegsId() {}
	
	public ItineraryLegsId(int reservation_id, String leg_id) {
		this.reservation_id = reservation_id;
		this.leg_id = leg_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leg_id == null) ? 0 : leg_id.hashCode());
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
		if (leg_id == null) {
			if (other.leg_id != null)
				return false;
		} else if (!leg_id.equals(other.leg_id))
			return false;
		if (reservation_id != other.reservation_id)
			return false;
		return true;
	}

	
}
