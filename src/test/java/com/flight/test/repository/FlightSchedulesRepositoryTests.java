package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import com.flight.entity.TravelClassCapacity;
import com.flight.repository.AirportsRepository;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.TravelClassCapacityRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class FlightSchedulesRepositoryTests {

	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Autowired
	private AirportsRepository airportsRepository;
	
	@Autowired
	private TravelClassCapacityRepository travelClassCapacityRepository;
	
	@Test
	@Rollback(false)
    @Order(1)
	public void testCreateNewFlightSchedule() {
		FlightSchedules flightSchedule = new FlightSchedules();
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
		
		flightSchedule.setUsual_aircraft_type_code(travelClassCapacity);
		
		flightSchedule.setOrigin_airport_code(originAirport);
		flightSchedule.setDestination_airport_code(destAirport);
		
		flightSchedule.setDeparture_date_time(Timestamp.valueOf(LocalDateTime.now()));
		LocalDateTime tom = LocalDateTime.now();
		flightSchedule.setArrival_date_time(Timestamp.valueOf(tom.plusDays(1)));
		
		FlightSchedules flightScheduleData = flightSchedulesRepository.save(flightSchedule);
		assertTrue(flightScheduleData.getOrigin_airport_code().getAirport_name().equals("Denver International Airport") 
				&& (flightScheduleData.getDestination_airport_code().getAirport_name().equals("Miami International Airport"))
				&& (flightScheduleData.getFlight_number() > 0));
	}

     
    @Test
    @Order(2)
    public void testListSchedules() {
        List<FlightSchedules> listOfSchedules = flightSchedulesRepository.findAll();
        assertTrue(listOfSchedules.size() > 0);
    }
     
    /*@Test
    @Rollback(false)
    @Order(3)
    public void testUpdateSchedule() {
    	Optional<FlightSchedules> scheduleData = flightSchedulesRepository.findById(1);
    	FlightSchedules schedule = scheduleData.get();
        schedule.setDeparture_date_time(Date.valueOf("2017-06-20"));
         
        flightSchedulesRepository.save(schedule);
         
        Optional<FlightSchedules> updatedScheduleData = flightSchedulesRepository.findById(1);
        FlightSchedules updatedSchedule = updatedScheduleData.get();
         
        assertTrue(updatedSchedule.getDeparture_date_time().equals("2017-06-20"));
    }*/
     
    /*@Test
    @Rollback(false)
    @Order(4)
    public void testDeleteSchedule() {
        flightSchedulesRepository.deleteById(1);
        
        Optional<FlightSchedules> deletedSchedules = flightSchedulesRepository.findById(1);
        assertTrue(!deletedSchedules.isPresent());
    }*/
	
}
