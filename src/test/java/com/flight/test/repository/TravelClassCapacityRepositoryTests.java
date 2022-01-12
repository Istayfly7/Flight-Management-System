package com.flight.test.repository;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.flight.entity.TravelClassCapacity;
import com.flight.repository.TravelClassCapacityRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TravelClassCapacityRepositoryTests {
	
	@Autowired
	private TravelClassCapacityRepository travelClassCapacityRepository;
	
	@Test
	//@Rollback(false)
	public void testCreateNewTravelClassCapacity() {
		TravelClassCapacity travelClassCapacity = new TravelClassCapacity();
		travelClassCapacity.setAircraft_type_code(1);
	
		TravelClassCapacity travelClassCapacityData = travelClassCapacityRepository.save(travelClassCapacity);
		assertTrue((travelClassCapacity.getSeat_capacity() == 40) && (travelClassCapacityData.getAircraft_type_code() > 0));
	}
	
}
