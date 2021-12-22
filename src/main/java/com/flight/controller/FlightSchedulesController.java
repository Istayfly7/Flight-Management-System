package com.flight.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.FlightSchedules;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.User;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.ItineraryReservationsRepository;
import com.flight.repository.UserRepository;

@RestController
@RequestMapping("flight_schedules")
public class FlightSchedulesController {

	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Autowired
	private ItineraryReservationsRepository itineraryReservationsRepository;
	
	@PostMapping("/save")
	public ResponseEntity<FlightSchedules> createNewFlight(@RequestBody FlightSchedules flightSchedules) {
		try {
			FlightSchedules f = flightSchedulesRepository.save(flightSchedules);
			return new ResponseEntity<>(f, HttpStatus.OK);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/remove/{flight_number}")
	public void removeFlight(@PathVariable("flight_number") int flight_number)
	{
		flightSchedulesRepository.delete(flightSchedulesRepository.getById(flight_number));
	}
	
	public ResponseEntity<List<ItineraryReservations>> viewReservations(int passenger_id) {
		try {
			List<ItineraryReservations> itineraryReservations = itineraryReservationsRepository.findAll();
			List<ItineraryReservations> listOfFlightSchedules = new ArrayList<>();
			
			for(ItineraryReservations itineraryReservation: itineraryReservations){
				if(itineraryReservation.getPassengerId() == passenger_id) {
					listOfFlightSchedules.add(itineraryReservation);
				}
			}
			
			if(!listOfFlightSchedules.isEmpty()) {
				return new ResponseEntity<>(listOfFlightSchedules, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/view/{passenger_id}")//*********************************NOT FINISHED
	public ResponseEntity<List<ItineraryReservations>> viewFlightSchedules(int passenger_id) {
		try {
			List<ItineraryReservations> listOfFlightSchedules = viewReservations(passenger_id).getBody();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
