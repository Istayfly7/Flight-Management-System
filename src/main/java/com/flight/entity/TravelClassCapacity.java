package com.flight.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.flight.id.TravelClassCapacityId;


@SuppressWarnings("serial")
@Entity
@IdClass(TravelClassCapacityId.class)
@Table(name="Travel_Class_Capacity")
public class TravelClassCapacity implements Serializable{
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int aircraft_type_code;
	
	@Id
	//@Column(nullable=false)
	private String travel_class_code; //= "10,30";
	
	//@Column(nullable=false)
	private int seat_capacity;
	
	public TravelClassCapacity() {}
	
	public TravelClassCapacity(int aircraft_type_code, String travel_class_code) {
		this.aircraft_type_code = aircraft_type_code;
		this.travel_class_code = travel_class_code;
		this.seat_capacity = addClassCodes(parseTravelClassCode());
	}
	
	public List<Integer> parseTravelClassCode(){
		List<Integer> listOfNum = new ArrayList<>();
		
		String[] list = this.travel_class_code.split(",");
		
		for(String v: list)
			listOfNum.add(Integer.parseInt(v));
		return listOfNum;
	}
	
	public int addClassCodes(List<Integer> classCodes) {
		return classCodes.get(0) + classCodes.get(1);
	}
	
	public void reserveASeat(int travelClass) {
		reserveSeats(1, travelClass);
	}
	
	public void reserveSeats(int num, int travelClass) {
		try {
			if(travelClass == 1) {
				if(parseTravelClassCode().get(0) - num >= 0) {
					this.travel_class_code = (parseTravelClassCode().get(0) - num) + "," + parseTravelClassCode().get(1).toString();
					this.seat_capacity -= num;
				}
				else
					throw new Exception("Not enough seats left to reserve " + num + " seats!");
			}
			else {
				if(parseTravelClassCode().get(1) - num >= 0) {
					this.travel_class_code = parseTravelClassCode().get(0).toString() + "," + (parseTravelClassCode().get(1) - num);
					this.seat_capacity -= num;
				}
				else
					throw new Exception("Not enough seats left to reserve " + num + " seats!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
		}
	}
	
	public void addASeat(int travelClass) {
		addSeats(1, travelClass);
	}
	
	public void addSeats(int num, int travelClass) {
		if(travelClass == 1)
			this.travel_class_code = (parseTravelClassCode().get(0) + num) + "," + parseTravelClassCode().get(1).toString();
		else
			this.travel_class_code = parseTravelClassCode().get(0).toString() + "," + (parseTravelClassCode().get(1) + num);
	}

	public int getAircraft_type_code() {
		return aircraft_type_code;
	}
	
	public void setAircraft_type_code(int aircraft_type_code) {
		this.aircraft_type_code = aircraft_type_code;
	}

	
 	public int getSeat_capacity() {
		return seat_capacity;
	}

	
	public String getTravel_class_code() {
		return travel_class_code;
	}
	

	public void setTravel_class_code(String travel_class_code) {
		this.travel_class_code = travel_class_code;
		this.seat_capacity = addClassCodes(parseTravelClassCode());
	}
	
	
	
	
}
