package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Booking_Agents")
public class BookingAgents {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int agent_id;
	
	@Column(nullable=false)
	private String agent_name;
	
	private String agent_details;
	
}
