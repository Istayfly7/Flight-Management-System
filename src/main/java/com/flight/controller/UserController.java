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
import org.springframework.web.bind.annotation.RestController;

import com.flight.repository.UserRepository;
import com.flight.entity.User;
import com.flight.helper.PrivilegeCheck;

@RestController
@RequestMapping("users")
public class UserController extends PrivilegeCheck {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/save")
	public ResponseEntity<User> createNewAccount(@RequestBody User user) {
		try {
			User u = userRepository.save(user);
			return new ResponseEntity<>(u, HttpStatus.OK);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//********************************************Admin************************************************
	
	@GetMapping("/viewAllUsers/{passenger_id}")
	public ResponseEntity<List<User>> viewUsers(@PathVariable("passenger_id") int passenger_id){
		try {
			if(privilegeCheck(passenger_id) == null)
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			else if(privilegeCheck(passenger_id) == Boolean.TRUE) {
				List<User> users = new ArrayList<>();
				userRepository.findAll().forEach(users::add);
			
				if(!users.isEmpty()) {
					return new ResponseEntity<>(users, HttpStatus.OK);
				}
				
				throw new Exception("no users found!");
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
