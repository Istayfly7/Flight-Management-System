package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.model.ReservationPayments;

@Entity
@Table(name="Payments")
public class Payments extends ReservationPayments{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int payment_id;
	
	//@OneToOne(mappedBy="payment_id")
	//private int payment_status_code;
	
	@Column(nullable=false)
	private int payment_date; //sql date object
	
	@Column(nullable=false)
	private int payment_amount; //short, int, or long
	
	public int getPaymentId() {
		return this.payment_id;
	}
}
