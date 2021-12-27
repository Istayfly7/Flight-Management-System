package com.flight.helper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.flight.entity.User;
import com.flight.repository.UserRepository;

public abstract class PrivilegeCheck {
	
	@Autowired
	private UserRepository userRepository;
	
	public Boolean privilegeCheck(int passenger_id) {
		Optional<User> userData = userRepository.findById(passenger_id);

		if(userData.isPresent()) {
			User u = userData.get();
			
			if(u.getType() == "EMPLOYEE")
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		}
		return null;
	}
}