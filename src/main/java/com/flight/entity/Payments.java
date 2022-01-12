package com.flight.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flight.model.ReservationPayments;

@Entity
@Table(name="Payments")
public class Payments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int payment_id;
	
	@OneToMany
	@JoinColumn(name="reservation_id")
	private List<ReservationPayments> payment_status_code;
	
	private Date payment_date; //sql date object
	
	private double payment_amount; //short, int, or long
	
	public int getPaymentId() {
		return this.payment_id;
	}

	public void setPayment_amount(double payment_amount) {
		this.payment_amount = payment_amount;
	}

	public double getPayment_amount() {
		return payment_amount;
	}

	public void setPayment_status_code(List<ReservationPayments> payment_status_code) {
		this.payment_status_code = payment_status_code;
	}

	public void setPayment_date(Date payment_date) {
		this.payment_date = payment_date;
	}

	public int getPayment_id() {
		return payment_id;
	}

	public List<ReservationPayments> getPayment_status_code() {
		return payment_status_code;
	}

	public Date getPayment_date() {
		return payment_date;
	}
	
}
