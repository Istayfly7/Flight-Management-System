package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flight.helper.TravelClassCapacityId;

@Entity
@IdClass(TravelClassCapacityId.class)
@Table(name="Travel_Class_Capacity")
public class TravelClassCapacity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int aircraft_type_code;
	
	@Id
	private int travel_class_code;
	
	@Column(nullable=false)
	private int seat_capacity = 30;
	
	/*{
		if(travel_class_code == 1)
			this.seat_capacity = 10;
	}*/
	
	public void reserveSeat() {
		reserveSeat(1);
	}
	
	public void reserveSeat(int num) {
		this.seat_capacity -= num;
	}
	
	public void addASeat() {
		addSeats(1);
	}
	
	public void addSeats(int num) {
		this.seat_capacity += num;
	}
	
	
}
