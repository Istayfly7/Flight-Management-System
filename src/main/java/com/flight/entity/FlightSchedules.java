package com.flight.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Flight_Schedules")
public class FlightSchedules {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int flight_number;
	
	/*@OneToOne(mappedBy="Flight_Schedules")
	private int airline_code;*/
}
