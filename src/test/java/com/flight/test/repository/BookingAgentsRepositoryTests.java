package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.flight.entity.BookingAgents;
import com.flight.repository.BookingAgentsRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookingAgentsRepositoryTests {
	
	@Autowired
	private BookingAgentsRepository bookingAgentsRepository;
	
	@Test
	//@Rollback(false)
	public void testCreateNewBookingAgent() {
		BookingAgents bookingAgent = new BookingAgents();
		bookingAgent.setAgent_name("Bob");
		bookingAgent.setAgent_details("");
		
		BookingAgents bookingAgentData = bookingAgentsRepository.save(bookingAgent);
		assertTrue(bookingAgentData.getAgent_name().equals("Bob") && (bookingAgentData.getAgent_id() > 0));
	}
}
