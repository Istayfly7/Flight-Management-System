package com.flight.model;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.entity.ItineraryReservations;
import com.flight.entity.Legs;

@Table(name="Itinerary_Legs")
public class ItineraryLegs {

	@OneToOne
	@JoinColumn(name="reservation_id")
	private ItineraryReservations reservation_id;
	
	@OneToOne
	@JoinColumn(name="leg_id")
	private Legs leg_id;
}
