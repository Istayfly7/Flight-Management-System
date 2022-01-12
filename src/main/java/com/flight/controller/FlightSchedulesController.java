package com.flight.controller;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.FlightSchedules;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.Legs;
import com.flight.entity.User;
import com.flight.helper.PrivilegeCheck;
import com.flight.model.FlightCosts;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.ItineraryReservationsRepository;
import com.flight.repository.LegsRepository;

@RestController
@RequestMapping("flight_schedules")
public class FlightSchedulesController extends PrivilegeCheck {

	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Autowired
	private ItineraryReservationsRepository itineraryReservationsRepository;

	@Autowired
	private LegsRepository legsRepository;
	
	
	@PostMapping("/save/{passenger_id}")
	public ResponseEntity<FlightSchedules> createNewFlight(@PathVariable("passenger_id") int passenger_id, @RequestBody FlightSchedules flightSchedules) {
		try {
			if(privilegeCheck(passenger_id) == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				FlightSchedules f = flightSchedulesRepository.save(flightSchedules);
				
				f.calculate();
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
	
	@DeleteMapping("/remove/{flight_number}")
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
	
	@GetMapping("/viewCustomers/{passenger_id}")
	public ResponseEntity<List<User>> viewCustomersOnFlight(@PathVariable("passenger_id") int passenger_id, @RequestParam int flight_number){
		try {
			if(privilegeCheck(passenger_id) == null)
				throw new Exception("Passenger not found!");
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				List<ItineraryReservations> listOfReservations = itineraryReservationsRepository.findAll();
				List<User> users = new ArrayList<>();
				
				for(ItineraryReservations itineraryReservation: listOfReservations) {
					if(itineraryReservation.getLeg_id().get(0).getFlight_Number().getFlight_number() == flight_number) {
						User user = itineraryReservation.getPassenger();
						if(user.getType() == "CUSTOMER")
							users.add(user);
					}
				}
				
				if(!users.isEmpty()) {
					return new ResponseEntity<>(users, HttpStatus.OK);
				}
				else
					throw new Exception("No users found on flight " + flight_number +   "!");
			}
			else
				throw new Exception("Passenger does not have access to this feature!");
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
					flightSchedules.add(listOfItineraryReservation.getLeg_id().get(0).getFlight_Number());
				}
				
				return new ResponseEntity<>(flightSchedules, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/view_Ontime_delay/{passenger_id}")
	public ResponseEntity<List<FlightSchedules>> viewOnTimeDelay(@PathVariable("passenger_id") int passenger_id, char code) {
		try {
			if(privilegeCheck(passenger_id) == null)
				throw new Exception("Passenger not found!");
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				List<FlightSchedules> listOfFlights = viewOnTimeDelay(code);
				
				if(!listOfFlights.isEmpty()) {
					return new ResponseEntity<>(listOfFlights, HttpStatus.OK);
				}
				else
					throw new Exception("List of flights are null or empty!");
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
	
	public List<FlightSchedules> viewOnTimeDelay(char code) {
		List<Legs> listOfLegs = legsRepository.findAll();
		List<FlightSchedules> listOfFlights = new ArrayList<>();
		boolean flag = false;
		
		try {
			LocalTime time;
			if(code == 'O' || code == 'o') {
				for(Legs leg: listOfLegs) {
					//if names equal, it means its the first leg
					if(leg.getOrigin_airport() == leg.getFlight_Number().getOrigin_airport_code().getAirport_name()) {
						time = leg.getFlight_Number().getDeparture_date_time().toLocalDateTime().toLocalTime();
						if(leg.getActual_departure_time() == Time.valueOf(time)) {
							flag = true;
						}
					}
					else if(leg.getDestination_airport() == leg.getFlight_Number().getDestination_airport_code().getAirport_name()) {
						time = leg.getFlight_Number().getArrival_date_time().toLocalDateTime().toLocalTime();
						if(leg.getActual_arrival_time() == Time.valueOf(time)) {
							if(flag == true) {
								listOfFlights.add(leg.getFlight_Number());
							}
						}
					}
				}
				return listOfFlights;
			}
			else if(code == 'D' || code == 'd') {
				for(Legs leg: listOfLegs) {
					//if names equal, it means its the first leg
					if(leg.getOrigin_airport() == leg.getFlight_Number().getOrigin_airport_code().getAirport_name()) {
						time = leg.getFlight_Number().getDeparture_date_time().toLocalDateTime().toLocalTime();
						if(leg.getActual_departure_time() != Time.valueOf(time)) {
							listOfFlights.add(leg.getFlight_Number());
						}
					}
					else if(leg.getDestination_airport() == leg.getFlight_Number().getDestination_airport_code().getAirport_name()) {
						time = leg.getFlight_Number().getArrival_date_time().toLocalDateTime().toLocalTime();
						if(leg.getActual_arrival_time() != Time.valueOf(time)) {
							listOfFlights.add(leg.getFlight_Number());
						}
					}
				}
				return listOfFlights;
			}
			else {
				throw new Exception("{Usage: \'O\'= On Time | \'D\'= Delayed}");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return null;
		}
	}
	
	@PutMapping("/update_fare/{admin_id}")
	public ResponseEntity<FlightCosts> updateFare(@PathVariable("admin_id") int admin_id, @RequestParam(required=true) int flight_number, @RequestParam(required=true) int newCost){
		try {
			if(privilegeCheck(admin_id) == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			else if(privilegeCheck(admin_id) == Boolean.TRUE) {
				Optional<FlightSchedules> flightScheduleData = flightSchedulesRepository.findById(flight_number);
				
				if(flightScheduleData.isPresent()) {
					FlightSchedules f = flightScheduleData.get();
					//FlightSchedules flightSchedule = i.getLeg_id().get(0).getFlight_Number();
					f.setFlight_cost(newCost);
					
					FlightCosts fc = flightSchedulesRepository.save(f);
					return new ResponseEntity<>(fc, HttpStatus.OK);
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