package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;
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

import com.flight.entity.Airports;
import com.flight.repository.AirportsRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AirportsRepositoryTests {

	@Autowired
	private AirportsRepository airportsRepository;
	
	@Test
	@Rollback(false)
    @Order(1)
	public void testCreateNewAirport() {
		Airports airport = new Airports();
		
		airport.setAirport_name("Denver International Airport");
		airport.setAirport_location("39.8561° N, 104.6737° W");
		airport.setOther_details("");
		
		Airports airportData = airportsRepository.save(airport);
		assertTrue(airportData.getAirport_name().equals("Denver International Airport") && (airportData.getAirport_code() > 0));
	}
	
	/*@Test
    @Order(2)
    public void testFindProductByName() {
        ...
    }*/
     
    /*@Test
    @Order(2)
    public void testListAirports() {
        List<Airports> airports = airportsRepository.findAll();
        assertTrue(airports.size() > 0);
    }*/
     
    /*@Test
    @Rollback(false)
    @Order(3)
    public void testUpdateAirport() {
    	Optional<Airports> airportData = airportsRepository.findById(1);
    	Airports airport = airportData.get();
        airport.setOther_details("updated");
         
        airportsRepository.save(airport);
        
        Optional<Airports> updatedAirportData = airportsRepository.findById(1);
        Airports updatedAirport = updatedAirportData.get();
         
        assertTrue(updatedAirport.getOther_details().equals("updated"));
    }*/
     
    @Test
    @Rollback(false)
    @Order(2)
    public void testDeleteAirport() {
        airportsRepository.deleteById(1);
         
        Optional<Airports> deletedAirport = airportsRepository.findById(1);
         
        assertTrue(!deletedAirport.isPresent());
    }
	
}
