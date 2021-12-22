package com.flight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Airports;
import com.flight.repository.AirportsRepository;

@RestController
@RequestMapping("airports")
public class AirportsController {

	@Autowired
	private AirportsRepository airportsRepository;
	
	@PostMapping("/save")
	public ResponseEntity<Airports> createNewAirport(@RequestBody Airports airport) {
		try {
			Airports a = airportsRepository.save(airport);
			return new ResponseEntity<>(a, HttpStatus.OK);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
