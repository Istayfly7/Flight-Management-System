package com.flight.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.flight.entity.FlightSchedules;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.User;
import com.flight.helper.PrivilegeCheck;
import com.flight.model.FlightCosts;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.ItineraryReservationsRepository;
import com.flight.repository.UserRepository;

@RestController
@RequestMapping("itinerary_reservations")
public class ItineraryReservationsController extends PrivilegeCheck{

	@Autowired
	private ItineraryReservationsRepository itineraryReservationsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/view/{passenger_id}")
	public ResponseEntity<ItineraryReservations> viewItinerary(@PathVariable("passenger_id") int passenger_id, @RequestParam int reservation_id){
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			Optional<ItineraryReservations> itineraryData = itineraryReservationsRepository.findById(reservation_id);
			
			if(userData.isPresent() && itineraryData.isPresent()) {
				User user = userData.get();
				ItineraryReservations itineraryReservation = itineraryData.get();
				
				if(user.getPassengerId() == itineraryReservation.getPassengerId()) {
					return new ResponseEntity<>(itineraryReservation, HttpStatus.OK);
				}
				else
					throw new Exception("Passenger does not have access to this feature!");
			}
			else {
				if(!userData.isPresent()) 
					throw new Exception("Passenger data not found!");
				else
					throw new Exception("Itinerary data not found!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/create_one_way/{passenger_id}")//use airports*********!!!!!!!!
	public ResponseEntity<Pair<Double, ItineraryReservations>> createOneWay(@PathVariable("passenger_id") int passenger_id, @RequestParam int ticketClass, @RequestParam int numberInParty, @RequestBody Airports from, @RequestBody Airports to) {
		Optional<User> userData = userRepository.findById(passenger_id);
		
		try {
			if(userData.isPresent()){
				User u = userData.get();
				
				ItineraryReservations itineraryReservation = new ItineraryReservations(u, 1, ticketClass, 0, numberInParty);
				Pair<Double, ItineraryReservations> pair = Pair.of(itineraryReservation.getLeg_id().get(0).getFlight_Number().getFlight_cost(), itineraryReservation);
				
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
	
	@GetMapping("/create_round_trip/{passenger_id}")
	public ResponseEntity<Pair<Double, List<ItineraryReservations>>> createRoundTrip(@PathVariable("passenger_id") int passenger_id, @RequestParam int ticketClass, @RequestParam int numberInParty, @RequestBody Airports from, @RequestBody Airports to) {
		Pair<Double, ItineraryReservations> firstWay = createOneWay(passenger_id, ticketClass, numberInParty, from, to).getBody();
		Pair<Double, ItineraryReservations> secWay = createOneWay(passenger_id, ticketClass, numberInParty, to, from).getBody();
		
		try {
			if(firstWay == null || secWay == null)
				throw new Exception("Atleast 1 way returned null!");
			
			double totPrice = firstWay.getFirst() + secWay.getFirst();
			
			Pair<Double, List<ItineraryReservations>> pair = Pair.of(totPrice, Arrays.asList(firstWay.getSecond(), secWay.getSecond()));
			return new ResponseEntity<>(pair, HttpStatus.OK);
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return null;
		}
	}
	
	@GetMapping("/create_multi_city/{passenger_id}")
	public ResponseEntity<Pair<Double, List<ItineraryReservations>>> createMultiCity(@PathVariable("passenger_id") int passenger_id, @RequestParam int ticketClass, @RequestParam int numberInParty, @RequestBody List<Airports> from, @RequestBody List<Airports> to) {
		List<Pair<Double, ItineraryReservations>> itineraryCostList = new ArrayList<>();
		List<ItineraryReservations> itineraryList = new ArrayList<>();
		double totPrice = 0.0;
		
		try {
			for(int i = 0; i < from.size(); i++) {
				itineraryCostList.add(createOneWay(passenger_id, ticketClass, numberInParty, from.get(i), to.get(i)).getBody());
			}
		
			for(int i = 0; i < itineraryCostList.size(); i++) {
				if(itineraryCostList.get(i) == null)
					throw new Exception("Atleast 1 way returned null!");
				else {
					totPrice += itineraryCostList.get(i).getFirst();
					itineraryList.add(itineraryCostList.get(i).getSecond());
				}
			}
			
			Pair<Double, List<ItineraryReservations>> pair = Pair.of(totPrice, itineraryList);
			return new ResponseEntity<>(pair, HttpStatus.OK);
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return null;
		}
	}
	
	@PostMapping("/save-one-way/{passenger_id}")
	public ResponseEntity<ItineraryReservations> confirm(@PathVariable("passenger_id") int passenger_id, @RequestParam double payment, @RequestBody ItineraryReservations itineraryReservations) {//set flight cost... confirm when payment status is ok and passenger_id match
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			
			if(userData.isPresent()) {
				User u = userData.get();
				if(u.getPassengerId() == itineraryReservations.getPassengerId()) {
					itineraryReservations.getReservation_status_code().setPayment_amount(payment);
					if((itineraryReservations.getLeg_id().get(0).getFlight_Number().getFlight_cost() * itineraryReservations.getNumber_in_party())  - itineraryReservations.getReservation_status_code().getPayment_amount() <= 0) {
						ItineraryReservations itineraryReservation = itineraryReservationsRepository.save(itineraryReservations);
					
						return new ResponseEntity<>(itineraryReservation, HttpStatus.OK);
					}
					else {
						throw new Exception("Itinerary not confirmed! Passenger did not pay the propper amount!");
					}
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
	
	@PostMapping("/save-round-trip/{passenger_id}")
	public ResponseEntity<List<ItineraryReservations>> confirmRoundTrip(@PathVariable("passenger_id") int passenger_id, @RequestParam double payment, @RequestBody List<ItineraryReservations> itineraryReservations) {//set flight cost... confirm when payment status is ok and passenger_id match
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			
			if(userData.isPresent()) {
				User u = userData.get();
				boolean flag = false;
				List<ItineraryReservations> ir = new ArrayList<>();
				
				for(ItineraryReservations itineraryReservation: itineraryReservations) {
					if(u.getPassengerId() == itineraryReservation.getPassengerId()) {
						itineraryReservation.getReservation_status_code().setPayment_amount(payment);
						
						
						if((itineraryReservation.getLeg_id().get(0).getFlight_Number().getFlight_cost() * itineraryReservation.getNumber_in_party())  - itineraryReservation.getReservation_status_code().getPayment_amount() <= 0) {
							if(flag == true) {
								ir = itineraryReservationsRepository.saveAll(itineraryReservations);
								
							}
							else
								flag = true;
							
						}
						else {
							throw new Exception("Itinerary not confirmed! Passenger did not pay the propper amount!");
						}
					}
					else{
						throw new Exception("Passenger does not have access to this feature!");
					}
				}
				
				return new ResponseEntity<>(ir, HttpStatus.OK);
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
	
	/*public double calculateCost(ItineraryReservations reservation) {
		FlightSchedules flightSchedule = reservation.getLeg_id().get(0).getFlight_Number();
				
		return flightSchedule.calculateOneWay(flightSchedule, reservation);
	}*/

}
