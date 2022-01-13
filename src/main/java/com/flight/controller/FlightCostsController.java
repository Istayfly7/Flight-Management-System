package com.flight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.FlightCosts;
import com.flight.helper.PrivilegeCheck;
import com.flight.repository.FlightCostsRepository;

@RestController
@RequestMapping("flight_costs")
public class FlightCostsController extends PrivilegeCheck{

		@Autowired
		private FlightCostsRepository flightCostsRepository;
		
		@PutMapping("/update_fare/{admin_id}")
		public ResponseEntity<FlightCosts> updateFare(@PathVariable("admin_id") int admin_id, @RequestParam(required=true) FlightCosts flightCosts, @RequestParam(required=true) int newCost){
			try {
				if(privilegeCheck(admin_id) == null) {
					return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
				}
				else if(privilegeCheck(admin_id) == Boolean.TRUE) {
					flightCosts.setFlight_cost(newCost);
						
					FlightCosts fc = flightCostsRepository.save(flightCosts);
					return new ResponseEntity<>(fc, HttpStatus.OK);
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
