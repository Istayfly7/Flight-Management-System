package com.flight.entity;

import java.time.LocalDate;
import java.util.List;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Itinerary_Reservations")
public class ItineraryReservations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reservation_id;
	
	@ManyToOne
	@JoinColumn(name="agent_id")
	private BookingAgents agent_id;
	
	@ManyToOne
	@JoinColumn(name="passenger_id")
	private User passenger_id;
	
	@OneToMany
	@JoinColumn(name="payment_id")
	private List<ReservationPayments> reservation_status_code;
	
	@OneToMany
	@JoinColumn(name="leg_id")
	private List<ItineraryLegs> ticket_type_code;
	
	//1st class: 10 seats, other class: 30
	@ManyToOne
	private TravelClassCapacity travel_class_code;
	
	private Date date_reservation_made;//sql date object
	
	private int number_in_party; //multiplier for payment amount
	
	public ItineraryReservations() {}
	
	public ItineraryReservations(User passenger_id, List<ItineraryLegs> ticket_type_code, TravelClassCapacity travel_class_code, int number_in_party) {
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

	public TravelClassCapacity getTravel_class_code() {
		return travel_class_code;
	}

	public List<ReservationPayments> getReservation_status_code() {
		return reservation_status_code;
	}

	public int getNumber_in_party() {
		return number_in_party;
	}

	public List<ItineraryLegs> getTicket_type_code() {
		return ticket_type_code;
	}

	public void setAgent_id(BookingAgents agent_id) {
		this.agent_id = agent_id;
	}

	public void setPassenger_id(User passenger_id) {
		this.passenger_id = passenger_id;
	}

	public void setReservation_status_code(List<ReservationPayments> reservation_status_code) {
		this.reservation_status_code = reservation_status_code;
	}

	public void setTicket_type_code(List<ItineraryLegs> ticket_type_code) {
		this.ticket_type_code = ticket_type_code;
	}

	public void setTravel_class_code(TravelClassCapacity travel_class_code) {
		this.travel_class_code = travel_class_code;
	}

	public void setDate_reservation_made(Date date_reservation_made) {
		this.date_reservation_made = date_reservation_made;
	}

	public void setNumber_in_party(int number_in_party) {
		this.number_in_party = number_in_party;
	}
	
}