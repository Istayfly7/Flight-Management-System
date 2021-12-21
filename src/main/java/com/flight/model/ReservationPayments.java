package com.flight.model;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.entity.ItineraryReservations;
import com.flight.entity.Payments;

@Table(name="Reservation_Payments")
public class ReservationPayments {
	
	@OneToOne
	@JoinColumn(name="reservation_id")
	private ItineraryReservations reservation_id;
	
	@OneToOne
	@JoinColumn(name="payment_id")
	private Payments payment_id;
}
