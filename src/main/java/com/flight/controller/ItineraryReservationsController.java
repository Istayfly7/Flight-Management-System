package com.flight.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Airports;
import com.flight.entity.FlightCosts;
import com.flight.entity.FlightSchedules;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.User;
import com.flight.helper.PrivilegeCheck;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.ItineraryReservationsRepository;
import com.flight.repository.UserRepository;

@RestController
@RequestMapping("itinerary_reservations")
public class ItineraryReservationsController extends PrivilegeCheck{

	@Autowired
	private ItineraryReservationsRepository itineraryReservationsRepository;
	
	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/create_one_way/{passenger_id}")
	public ResponseEntity<Pair<Double, ItineraryReservations>> createOneWay(@PathVariable("passenger_id") int passenger_id, @RequestParam int ticketClass, @RequestParam int numberInParty, @RequestBody Airports from, @RequestBody Airports to) {
		Optional<User> userData = userRepository.findById(passenger_id);
		
		try {
			if(userData.isPresent()){
				User u = userData.get();
				
				ItineraryReservations itineraryReservation = new ItineraryReservations(u, 1, ticketClass, 0, numberInParty);
				Pair<Double, ItineraryReservations> pair = Pair.of(Double.valueOf((calculateCost(itineraryReservation))), itineraryReservation);
				
				return new ResponseEntity<>(pair, HttpStatus.OK);
			}
			else {
				throw new Exception("Passenger id not found!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return null;
		}
		
	}
	
	@PostMapping("/save/{passenger_id}")
	public ResponseEntity<ItineraryReservations> confirm(@PathVariable("passenger_id") int passenger_id, @RequestBody ItineraryReservations itineraryReservations) {//set flight cost
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			
			if(userData.isPresent()) {
				User u = userData.get();
				if(u.getPassengerId() == itineraryReservations.getPassengerId()) {
					ItineraryReservations itineraryReservation = itineraryReservationsRepository.save(itineraryReservations);
					return new ResponseEntity<>(itineraryReservation, HttpStatus.OK);
				}
				else{
					throw new Exception("Passenger does not have access to this feature!");
				}
			}
			else {
				throw new Exception("Passenger id not found!");
			}
				
			
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return null;
		}
	}
	
	public double calculateCost(ItineraryReservations reservation) {
		FlightSchedules flightSchedule = reservation.getLeg_id().get(0).getFlight_Number();
				
		return flightSchedule.calculateOneWay(flightSchedule, reservation);
	}
	
	@PutMapping("/update_fare/{admin_id}")
	public ResponseEntity<FlightCosts> updateFare(@PathVariable("admin_id") int admin_id, @RequestParam(required=true) int reservation_id, @RequestParam(required=true) int newCost){
		try {
			if(privilegeCheck(admin_id) == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			else if(privilegeCheck(admin_id) == Boolean.TRUE) {
				Optional<ItineraryReservations> itineraryData = itineraryReservationsRepository.findById(reservation_id);
				
				if(itineraryData.isPresent()) {
					ItineraryReservations i = itineraryData.get();
					FlightSchedules flightSchedule = i.getLeg_id().get(0).getFlight_Number();
					flightSchedule.setFlight_cost(newCost);
					
					FlightCosts f = flightSchedulesRepository.save(flightSchedule);
					return new ResponseEntity<>(f, HttpStatus.OK);
				}
				else {
					throw new Exception("Passenger id not found!");
				}
				
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
