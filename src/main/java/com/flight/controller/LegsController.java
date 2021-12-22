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
import com.flight.repository.LegsRepository;

@RestController
@RequestMapping("legs")
public class LegsController {

	@Autowired
	private LegsRepository legsRepository;
	
	@PostMapping("/save")
	public ResponseEntity<Legs> createNewLeg(@RequestBody Legs leg) {
		try {
			Legs l = legsRepository.save(leg);
			return new ResponseEntity<>(l, HttpStatus.OK);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update_departure/{leg_id}")
	public ResponseEntity<Legs> updateDeparture(@RequestParam(required=true) int departureTime, @PathVariable(name="leg_id") int legId){
		try {
			Optional<Legs> legData = legsRepository.findById(legId);
			
			if(legData.isPresent()){
				Legs leg = legData.get();
				
				leg.setActual_departure_time(departureTime);
				
				legsRepository.save(leg);
				
				return new ResponseEntity<>(leg, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update_arrival/{leg_id}")
	public ResponseEntity<Legs> updateArrival(@RequestParam(required=true) int arrivalTime, @PathVariable(name="leg_id") int legId){
		try {
			Optional<Legs> legData = legsRepository.findById(legId);
			
			if(legData.isPresent()){
				Legs leg = legData.get();
				
				leg.setActual_arrival_time(arrivalTime);
				
				legsRepository.save(leg);
				
				return new ResponseEntity<>(leg, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
