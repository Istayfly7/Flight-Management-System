package com.flight.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.model.ItineraryLegs;

@Entity
@Table(name="Itinerary_Reservations")
public class ItineraryReservations extends ItineraryLegs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reservation_id;
	
	@OneToOne
	@JoinColumn(name="agent_id")
	private BookingAgents agent_id;
	
	@OneToOne
	@JoinColumn(name="passenger_id")
	private User passenger_id;
	
	@OneToOne
	@JoinColumn(name="payment_id")
	private Payments reservation_status_code;
	
	/*@OneToOne(mappedBy="reservation_id")
	private int ticket_type_code;*/
	
	@OneToMany
	@JoinColumn(name="travel_class_code")
	private List<TravelClassCapacity> travel_class_code;
	
	@Column(nullable=false)
	private int date_reservation_made;//sql date object
	
	private int number_in_party;

	public int getPassengerId() {
		return this.passenger_id.getPassengerId();
	}
	
	public int getReservationId(){
		return this.reservation_id;
	}
	
	public User getPassenger() {
		return this.passenger_id;
	}
}