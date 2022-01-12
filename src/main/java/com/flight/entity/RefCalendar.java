package com.flight.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flight.model.FlightCosts;

@Entity
@Table(name="Ref_Calendar")
public class RefCalendar {

	@Id
	//@GeneratedValue(strategy = GenerationType.)
	private Date day_date = Date.valueOf(LocalDate.now()); //sql date object
	
	@OneToMany
	@JoinColumn
	private List<FlightCosts> flightCosts;
	
	@Column(nullable=false)
	private int day_number;
	
	@Column(nullable=false)
	private int business_day_yn;
}
