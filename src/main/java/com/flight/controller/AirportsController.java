package com.flight.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Airports;
import com.flight.entity.FlightSchedules;
import com.flight.helper.PrivilegeCheck;
import com.flight.repository.AirportsRepository;
import com.flight.repository.FlightSchedulesRepository;

@RestController
@RequestMapping("airports")
public class AirportsController extends PrivilegeCheck{

	@Autowired
	private AirportsRepository airportsRepository;
	
	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@PostMapping("/save/{passenger_id}")
	public ResponseEntity<Airports> createNewAirport(@PathVariable("passenger_id") int passenger_id, @RequestBody Airports airport) {
		try {
			if(privilegeCheck(passenger_id) == null)
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				Airports a = airportsRepository.save(airport);
				return new ResponseEntity<>(a, HttpStatus.OK);
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
	
	@GetMapping("/viewFlights/{passenger_id}")
	public ResponseEntity<List<FlightSchedules>> viewFlights(@PathVariable("passenger_id") int passenger_id, @RequestParam(required=true) int airport_code){
		try {
			if(privilegeCheck(passenger_id) == null)
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				List<FlightSchedules> flightSchedules = flightSchedulesRepository.findAll();
				List<FlightSchedules> listOfFlights = new ArrayList<>();
			
				for(FlightSchedules flight: flightSchedules) {
					if((flight.getOrigin_airport_code().getAirport_code() == airport_code)
						|| (flight.getDestination_airport_code().getAirport_code() == airport_code)) {
						listOfFlights.add(flight);
					}
				}
			
				if(!listOfFlights.isEmpty())
					return new ResponseEntity<>(listOfFlights, HttpStatus.OK);
				else
					throw new Exception("No flight schedule data found for this airport!");
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
	
}
