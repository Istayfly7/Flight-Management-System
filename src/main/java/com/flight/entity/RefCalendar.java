package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Ref_Calendar")
public class RefCalendar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int day_date; //sql date object
	
	@Column(nullable=false)
	private int day_number;
	
	@Column(nullable=false)
	private int business_day_yn;
}
