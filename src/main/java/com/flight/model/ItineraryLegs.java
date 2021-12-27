package com.flight.model;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.entity.ItineraryReservations;
import com.flight.entity.Legs;

@Table(name="Itinerary_Legs")
public abstract class ItineraryLegs {

	@OneToOne
	@JoinColumn(name="reservation_id")
	private ItineraryReservations reservation_id;
	
	@OneToMany
	@JoinColumn(name="leg_id")
	private List<Legs> leg_id;

	public List<Legs> getLeg_id() {
		return this.leg_id;
	}
	
	public ItineraryReservations getReservation_id() {
		return this.reservation_id;
	}
	
}
