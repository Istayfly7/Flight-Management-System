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

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public String getAgent_details() {
		return agent_details;
	}

	public void setAgent_details(String agent_details) {
		this.agent_details = agent_details;
	}

	public int getAgent_id() {
		return agent_id;
	}
	
}
