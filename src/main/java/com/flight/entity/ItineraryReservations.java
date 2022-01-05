package com.flight.entity;

import java.time.LocalDate;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	
	//One-way, Round-trip, Muli-city
	@Column(nullable=false)
	private int ticket_type_code;
	
	//1st class: 10 seats, other class: 30
	@Column(nullable=false)
	private int travel_class_code;
	
	@Column(nullable=false)
	private Date date_reservation_made;//sql date object
	
	private int number_in_party; //multiplier for payment amount
	
	public ItineraryReservations() {}
	
	public ItineraryReservations(User passenger_id, int ticket_type_code, int travel_class_code, int number_in_party) {
		this.passenger_id = passenger_id;
		this.ticket_type_code = ticket_type_code;
		this.travel_class_code = travel_class_code;
		this.date_reservation_made = Date.valueOf(LocalDate.now());
		this.number_in_party = number_in_party;
	}

	public int getPassengerId() {
		return this.passenger_id.getPassengerId();
	}
	
	public int getReservationId(){
		return this.reservation_id;
	}
	
	public User getPassenger() {
		return this.passenger_id;
	}

	public int getTravel_class_code() {
		return travel_class_code;
	}

	public int getTicket_type_code() {
		return ticket_type_code;
	}

	public Payments getReservation_status_code() {
		return reservation_status_code;
	}

	public int getNumber_in_party() {
		return number_in_party;
	}
	
}