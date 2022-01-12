package com.flight.id;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReservationPaymentsId implements Serializable{

	private int reservation_id;
	
	private int payment_id;
	
	public ReservationPaymentsId() {}
	
	public ReservationPaymentsId(int reservation_id, int payment_id) {
		this.reservation_id = reservation_id;
		this.payment_id = payment_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + payment_id;
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
		ReservationPaymentsId other = (ReservationPaymentsId) obj;
		if (payment_id != other.payment_id)
			return false;
		if (reservation_id != other.reservation_id)
			return false;
		return true;
	}
	
	
}
