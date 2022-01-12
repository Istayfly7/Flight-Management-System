package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
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
	@Rollback(false)
    @Order(1)
	public void testCreateNewLeg() {
		Legs leg = new Legs();
		
		TravelClassCapacity travelClassCapacity = new TravelClassCapacity();
		travelClassCapacityRepository.save(travelClassCapacity);
		
		Airports originAirport = new Airports();
		originAirport.setAirport_name("Denver International Airport");
		originAirport.setAirport_location("39.8561° N, 104.6737° W");
		originAirport.setOther_details("");
		Airports destAirport = new Airports();
		destAirport.setAirport_name("Miami International Airport");
		destAirport.setAirport_location("25.7969° N, 80.2762° W");
		destAirport.setOther_details("");
		airportsRepository.save(originAirport);
		airportsRepository.save(destAirport);
		
		FlightSchedules flightSchedule = new FlightSchedules();
		flightSchedule.setUsual_aircraft_type_code(travelClassCapacity);
		
		flightSchedule.setOrigin_airport_code(originAirport);
		flightSchedule.setDestination_airport_code(destAirport);
		
		flightSchedule.setDeparture_date_time(Timestamp.valueOf(LocalDateTime.now()));
		LocalDateTime tom = LocalDateTime.now();
		flightSchedule.setArrival_date_time(Timestamp.valueOf(tom.plusDays(1)));
		flightSchedulesRepository.save(flightSchedule);
		
		leg.setFlight_number(flightSchedule);
		leg.setOrigin_airport("Denver International Airport");
		leg.setDestination_airport("Miami International Airport");
		leg.setActual_departure_time(Time.valueOf(LocalTime.now()));
		LocalTime twoHours = LocalTime.now();
		leg.setActual_arrival_time(Time.valueOf(twoHours.plusHours(2)));
		
		Legs legData = legsRepository.save(leg);
		assertTrue(legData.getOrigin_airport().equals("Denver International Airport")
				&& (legData.getDestination_airport().equals("Miami International Airport"))
				&& (legData.getLeg_id() > 0));
	}
	
	@Test
    @Order(2)
    public void testListSchedules() {
        List<Legs> listOfLegs = legsRepository.findAll();
        assertTrue(listOfLegs.size() > 0);
    }
	
}
