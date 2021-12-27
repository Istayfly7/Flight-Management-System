package com.flight.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.FlightSchedules;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.Legs;
import com.flight.helper.PrivilegeCheck;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.ItineraryReservationsRepository;

@RestController
@RequestMapping("flight_schedules")
public class FlightSchedulesController extends PrivilegeCheck {

	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Autowired
	private ItineraryReservationsRepository itineraryReservationsRepository;
	
	@PostMapping("/save/{passenger_id}")//*************************************
	public ResponseEntity<FlightSchedules> createNewFlight(@PathVariable("passenger_id") int passenger_id, FlightSchedules flightSchedules) {
		try {
			if(privilegeCheck(passenger_id) == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				FlightSchedules f = flightSchedulesRepository.save(flightSchedules);
				return new ResponseEntity<>(f, HttpStatus.OK);
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
	
	@DeleteMapping("/remove/{flight_number}")//*************************************
	public void removeFlight(@PathVariable("flight_number") int flight_number, @RequestParam int passenger_id)
	{
		try {
			if(privilegeCheck(passenger_id) == null) {
				throw new Exception("Passenger not found!");
			}
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				flightSchedulesRepository.delete(flightSchedulesRepository.getById(flight_number));
			}
			else {
				throw new Exception("Passenger does not have access to this feature!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
		}
	}
	
	public List<ItineraryReservations> viewReservations(int passenger_id) {
		try {
			List<ItineraryReservations> itineraryReservations = itineraryReservationsRepository.findAll();
			List<ItineraryReservations> listOfFlightSchedules = new ArrayList<>();
			
			for(ItineraryReservations itineraryReservation: itineraryReservations){
				if(itineraryReservation.getPassengerId() == passenger_id) {
					listOfFlightSchedules.add(itineraryReservation);
				}
			}
			
			if(!listOfFlightSchedules.isEmpty()) {
				return listOfFlightSchedules;
			}
			else {
				throw new Exception("Itinerary reservations are empty. You have no reservations!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return null;
		}
	}
	
	@GetMapping("/view-legs/{passenger_id}")
	public ResponseEntity<Map<Integer, List<Legs>>> viewLegs(@PathVariable("passenger_id") int passenger_id) {
		try {
			List<ItineraryReservations> listOfItineraryReservations = viewReservations(passenger_id);
			
			if(!listOfItineraryReservations.isEmpty()) {
				Map<Integer, List<Legs>> legsMap = new LinkedHashMap<>();
				
				for(int i= 0; i < listOfItineraryReservations.size(); i++) {
					legsMap.put(i, listOfItineraryReservations.get(i).getLeg_id());
				}
				
				return new ResponseEntity<>(legsMap, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/view/{passenger_id}")
	public ResponseEntity<List<FlightSchedules>> viewFlightSchedules(@PathVariable("passenger_id") int passenger_id) {
		try {
			List<ItineraryReservations> listOfItineraryReservations = viewReservations(passenger_id);
			
			if(!listOfItineraryReservations.isEmpty()) {
				List<FlightSchedules> flightSchedules = new ArrayList<>();
				
				for(ItineraryReservations listOfItineraryReservation: listOfItineraryReservations) {
					flightSchedules.add(listOfItineraryReservation.getLeg_id().get(0).getFlightSchedule());
				}
				
				return new ResponseEntity<>(flightSchedules, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
