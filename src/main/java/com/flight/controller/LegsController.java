package com.flight.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Legs;
import com.flight.helper.PrivilegeCheck;
import com.flight.repository.LegsRepository;

@RestController
@RequestMapping("legs")
public class LegsController extends PrivilegeCheck {

	@Autowired
	private LegsRepository legsRepository;

	
	@PostMapping("/save/{passenger_id}")
	public ResponseEntity<Legs> createNewLeg(@PathVariable("passenger_id") int passenger_id, @RequestBody Legs leg) {
		try {
			if(privilegeCheck(passenger_id) == null)
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			else if(privilegeCheck(passenger_id) == Boolean.TRUE)
				return new ResponseEntity<>(legsRepository.save(leg), HttpStatus.OK);
			else
				throw new Exception("Passenger does not have access to this feature!");
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update_departure/{leg_id}")
	public ResponseEntity<Legs> updateDeparture(@RequestParam(required=true) int passenger_id, @RequestParam(required=true) int departureTime, @PathVariable(name="leg_id") int legId){
		try {
			if(privilegeCheck(passenger_id) == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				Optional<Legs> legData = legsRepository.findById(legId);
				
				if(legData.isPresent()){
					Legs leg = legData.get();
					
					leg.setActual_departure_time(departureTime);
					
					legsRepository.save(leg);
					
					return new ResponseEntity<>(leg, HttpStatus.OK);
				}
				else
					throw new Exception("No leg data found!");
			}
			else {
				throw new Exception("Passenger does not have access to this feature!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update_arrival/{leg_id}")
	public ResponseEntity<Legs> updateArrival(@RequestParam(required=true) int arrivalTime, @RequestParam(required=true) int passenger_id, @PathVariable(name="leg_id") int legId){
		try {
			if(privilegeCheck(passenger_id) == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				Optional<Legs> legData = legsRepository.findById(legId);
			
			
				if(legData.isPresent()){
					Legs leg = legData.get();
				
					leg.setActual_arrival_time(arrivalTime);
				
					legsRepository.save(leg);
				
					return new ResponseEntity<>(leg, HttpStatus.OK);
				}
				else
					throw new Exception("No leg data found!");
				}
			else {
				throw new Exception("Passenger does not have access to this feature!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
