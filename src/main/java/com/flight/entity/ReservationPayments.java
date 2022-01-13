package com.flight.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.flight.id.ReservationPaymentsId;

@SuppressWarnings("serial")
@Entity
@Table(name="Reservation_Payments")
@IdClass(ReservationPaymentsId.class)
public class ReservationPayments implements Serializable{
	
	//@OneToOne
	//@JoinColumn(name="reservation_id")
	@Id
	private int reservation_id;
	
	//@OneToOne
	//@JoinColumn(name="payment_id")
	@Id
	private int payment_id;
	
	public ReservationPayments() {}
	
	public ReservationPayments(int reservation_id, int payment_id) {
		this.reservation_id = reservation_id;
		this.payment_id = payment_id;
	}

	public int getReservation_id() {
		return reservation_id;
	}

	public int getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}

	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}

}
