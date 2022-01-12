package com.flight.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.flight.id.ItineraryLegsId;

@SuppressWarnings("serial")
@Entity
@Table(name="Itinerary_Legs")
@IdClass(ItineraryLegsId.class)
public class ItineraryLegs implements Serializable{

	//@OneToOne
	//@JoinColumn(name="reservation_id")
	@Id
	private int reservation_id;
	
	//@OneToMany(mappedBy="ItineraryLegs")
	@Id
	private int leg_id;

	public int getLeg_id() {
		return this.leg_id;
	}
	
	public void setLeg_id(int leg_id) {
		this.leg_id = leg_id;
	}

	public int getReservation_id() {
		return this.reservation_id;
	}
	
}
