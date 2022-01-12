package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.flight.entity.Airports;
import com.flight.entity.FlightSchedules;
import com.flight.entity.Legs;
import com.flight.entity.TravelClassCapacity;
import com.flight.repository.AirportsRepository;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.LegsRepository;
import com.flight.repository.TravelClassCapacityRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LegsRepositoryTests {

	@Autowired
	private LegsRepository legsRepository;
	
	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Autowired
	private AirportsRepository airportsRepository;
	
	@Autowired
	private TravelClassCapacityRepository travelClassCapacityRepository;
	
	@Test
	//@Rollback(false)
	public void testCreateNewLeg() {
		TravelClassCapacity travelClassCapacity = new TravelClassCapacity();
		travelClassCapacityRepository.save(travelClassCapacity);
		
		Airports from = new Airports();
		Airports to = new Airports();
		from.setAirport_name("Denver International Airport");
		to.setAirport_name("Miami International Airport");
		from.setAirport_location("39.8561° N, 104.6737° W");
		to.setAirport_location("25.7969° N, 80.2762° W");
		airportsRepository.save(from);
		airportsRepository.save(to);
		
		FlightSchedules flightSchedule = new FlightSchedules();
		flightSchedule.setUsual_aircraft_type_code(travelClassCapacity);
		flightSchedule.setOrigin_airport_code(from);
		flightSchedule.setDestination_airport_code(to);
		flightSchedule.setDeparture_date_time(Timestamp.valueOf(LocalDateTime.now()));
		LocalDateTime tom = LocalDateTime.now();
		flightSchedule.setArrival_date_time(Timestamp.valueOf(tom.plusHours(2)));
		flightSchedulesRepository.save(flightSchedule);
		
		Legs leg = new Legs(flightSchedule);
		Legs legData = legsRepository.save(leg);
		assertTrue(legData.getOrigin_airport().equals("Denver International Airport")
				&& (legData.getDestination_airport().equals("Miami International Airport"))
				&& (legData.getLeg_id() > 0));
	}
	
}
