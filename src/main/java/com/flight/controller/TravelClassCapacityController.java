package com.flight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.TravelClassCapacity;
import com.flight.repository.TravelClassCapacityRepository;

@RestController
@RequestMapping("travel_class_capacity")
public class TravelClassCapacityController {
	
	@Autowired
	private TravelClassCapacityRepository travelClassCapacityRepository;
	
	@PostMapping("/save")
	public ResponseEntity<TravelClassCapacity> createNewAccount(@RequestBody TravelClassCapacity travelClassCapacity) {
		try {
			TravelClassCapacity t = travelClassCapacityRepository.save(travelClassCapacity);
			return new ResponseEntity<>(t, HttpStatus.OK);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
