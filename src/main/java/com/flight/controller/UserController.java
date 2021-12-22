package com.flight.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.repository.UserRepository;
import com.flight.entity.User;

@RestController
@RequestMapping("users")
public class UserController {

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
	
	@GetMapping("/viewAllUsers")
	public ResponseEntity<List<User>> viewUsers(/*@RequestBody User user(check for EMPLOYEE type)*/){
		try {
			List<User> users = new ArrayList<>();
			userRepository.findAll().forEach(users::add);
			
			if(!users.isEmpty()) {
				return new ResponseEntity<>(users, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
