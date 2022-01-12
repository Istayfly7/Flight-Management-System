package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.flight.entity.CustomerUser;
import com.flight.entity.EmployeeUser;
import com.flight.entity.User;
import com.flight.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Rollback(false)
    @Order(1)
	public void testCreateNewEmployee() {
		User adminUser = new EmployeeUser();
		adminUser.setFirst_name("Employ");
		adminUser.setSecond_name("E");
		adminUser.setLast_name("Employee");
		adminUser.setPhone_number(000-000-0000);
		adminUser.setEmail_address("employee@gmail.com");
		adminUser.setAddress_lines("123 Nonsense Lane");
		adminUser.setCity("Anywhere");
		adminUser.setState_province_county("");
		adminUser.setCountry("US");
		adminUser.setOther_passenger_details("");
		

		User adminUserData = userRepository.save(adminUser);
		assertTrue(adminUserData.getType().equals("EMPLOYEE") && (adminUserData.getPassengerId() > 0));
	}
	
	@Test
	@Rollback(false)
    @Order(2)
	public void testCreateNewCustomer() {
		User passengerUser = new CustomerUser();
		passengerUser.setFirst_name("Cust");
		passengerUser.setSecond_name("c");
		passengerUser.setLast_name("Customer");
		passengerUser.setPhone_number(000-000-0000);
		passengerUser.setEmail_address("customer@gmail.com");
		passengerUser.setAddress_lines("123 Nonsense Lane");
		passengerUser.setCity("Anywhere");
		passengerUser.setState_province_county("");
		passengerUser.setCountry("US");
		passengerUser.setOther_passenger_details("");
		
		User passengerUserData = userRepository.save(passengerUser);
		assertTrue(passengerUserData.getType().equals("CUSTOMER") && (passengerUserData.getPassengerId() > 0));
	}

	/*@Test
    @Order(3)
    public void testListUsers() {
		List<User> users = userRepository.findAll();
		System.out.println("size: " + users.size());
		assertTrue(users.size() == 2);
    }
     
    @Test
    @Rollback(false)
    @Order(4)
    public void testUpdateEmployee() {
        ...
    }
    
    @Test
    @Rollback(false)
    @Order(5)
    public void testUpdateCustomer() {
        ...
    }*/
     
    /*@Test
    @Rollback(false)
    @Order(3)
    public void testDeleteEmployee() {
       userRepository.deleteById(1);
       
       Optional<User> deletedUser = userRepository.findById(1);
       assertTrue(!deletedUser.isPresent());
    }*/
    
    @Test
    @Rollback(false)
    @Order(4)
    public void testDeleteCustomer() {
    	userRepository.deleteById(2);
        
        Optional<User> deletedUser = userRepository.findById(2);
        assertTrue(!deletedUser.isPresent());
    }
	
}
