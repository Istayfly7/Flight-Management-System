package com.flight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.BookingAgents;
import com.flight.helper.PrivilegeCheck;
import com.flight.repository.BookingAgentsRepository;

@RestController
@RequestMapping("booking_agents")
public class BookingAgentsController extends PrivilegeCheck{

	@Autowired
	private BookingAgentsRepository bookingAgentsRepository;
	
	@PostMapping("/save/{passenger_id}")
	public ResponseEntity<BookingAgents> createNewBookingAgent(@PathVariable("passenger_id") int passenger_id, @RequestBody BookingAgents bookingAgents) {
		try {
			if(privilegeCheck(passenger_id) == null)
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			else if(privilegeCheck(passenger_id) == Boolean.TRUE)
				return new ResponseEntity<>(bookingAgentsRepository.save(bookingAgents), HttpStatus.OK);
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
