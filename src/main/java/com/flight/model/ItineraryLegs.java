package com.flight.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.flight.id.ItineraryLegsId;

@SuppressWarnings("serial")
@Entity
@Table(name="Itinerary_Legs")
@IdClass(ItineraryLegsId.class)
public class ItineraryLegs implements Serializable{

	@Id
	private int reservation_id;
	
	//private int leg_id;
	@Id
	private String leg_id;

	public ItineraryLegs() {}
	
	public ItineraryLegs(int reservation_id, String leg_id) {
		this.reservation_id = reservation_id;
		this.leg_id = leg_id;
	}
	
	public List<Integer> getLegIds() {
		String[] legs = leg_id.split(",");
		List<Integer> listOfLegs = new ArrayList<>();
		
		for(String leg: legs) {
			listOfLegs.add(Integer.parseInt(leg));
		}
		return listOfLegs;
	}
	
	public void setLeg_id(List<Integer> leg_id) {
		String legs = "";
		for(int i = 0; i < leg_id.size(); i++) {
			if(i == leg_id.size()-1) {
				legs += leg_id.get(i);
			}
			else {
				legs += leg_id.get(i) + ",";
			}
		}
		
		this.leg_id = legs.toString();
	}
	
	/*public void setLeg_id(String leg_id) {
		this.leg_id = leg_id;
	}*/

	public int getReservation_id() {
		return this.reservation_id;
	}

	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}
	
}
