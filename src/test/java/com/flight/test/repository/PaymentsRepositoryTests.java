package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.flight.entity.Airports;
import com.flight.entity.BookingAgents;
import com.flight.entity.CustomerUser;
import com.flight.entity.FlightSchedules;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.Legs;
import com.flight.entity.Payments;
import com.flight.entity.TravelClassCapacity;
import com.flight.entity.User;
import com.flight.model.ItineraryLegs;
import com.flight.model.ReservationPayments;
import com.flight.repository.AirportsRepository;
import com.flight.repository.BookingAgentsRepository;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.ItineraryLegsRepository;
import com.flight.repository.ItineraryReservationsRepository;
import com.flight.repository.LegsRepository;
import com.flight.repository.PaymentsRepository;
import com.flight.repository.ReservationPaymentsRepository;
import com.flight.repository.TravelClassCapacityRepository;
import com.flight.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PaymentsRepositoryTests {
	
	@Autowired
	private PaymentsRepository paymentsRepository;
	
	@Autowired
	private BookingAgentsRepository bookingAgentsRepository;
	
	@Autowired
	private ItineraryReservationsRepository itineraryReservationsRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReservationPaymentsRepository reservationPaymentsRepository;

	@Autowired
	private ItineraryLegsRepository itineraryLegsRepository;
	
	@Autowired
	private TravelClassCapacityRepository travelClassCapacityRepository;

	@Autowired
	private LegsRepository legsRepository;
	
	@Autowired
	private AirportsRepository airportsRepository;

	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Test
	//@Rollback(false)
	public void testCreateNewPayment() {
		BookingAgents agent = new BookingAgents();
		agent.setAgent_name("John");
		agent.setAgent_details("");
		bookingAgentsRepository.save(agent);
		
		User passenger = new CustomerUser();
		passenger.setFirst_name("Barry");
		passenger.setLast_name("Jones");
		passenger.setPhone_number(000-000-0000);
		passenger.setEmail_address("nobody@gmail.com");
		passenger.setAddress_lines("123 Anywhere Lane");
		passenger.setCity("Fort Collins");
		passenger.setState_province_county("Larimer");
		passenger.setCountry("US");
		userRepository.save(passenger);
		
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
		
		ItineraryReservations itineraryReservation = new ItineraryReservations();
		itineraryReservation.setAgent_id(agent);
		itineraryReservation.setPassenger_id(passenger);
		
		Payments payment = new Payments();
		paymentsRepository.save(payment);
		List<ReservationPayments> listOfPayments = new ArrayList<>();
		ReservationPayments reservationPayment = new ReservationPayments();
		
		reservationPayment.setPayment_id(1);
		reservationPayment.setReservation_id(itineraryReservation.getReservationId());
		reservationPaymentsRepository.save(reservationPayment);
		listOfPayments.add(reservationPayment);
		
		FlightSchedules flightSchedule = new FlightSchedules();
		flightSchedule.setUsual_aircraft_type_code(travelClassCapacity);
		flightSchedule.setOrigin_airport_code(from);
		flightSchedule.setDestination_airport_code(to);
		flightSchedule.setDeparture_date_time(Timestamp.valueOf(LocalDateTime.now()));
		LocalDateTime tom = LocalDateTime.now();
		flightSchedule.setArrival_date_time(Timestamp.valueOf(tom.plusHours(2)));
		flightSchedulesRepository.save(flightSchedule);
		
		
		Legs leg = new Legs(flightSchedule);
		legsRepository.save(leg);
		List<Integer> legIds = new ArrayList<>();
		legIds.add(leg.getLeg_id());
		
		List<ItineraryLegs> listOfLegs = new ArrayList<>();
		ItineraryLegs itLeg = new ItineraryLegs();
		itLeg.setLeg_id(legIds);
		itLeg.setReservation_id(itineraryReservation.getReservationId());
		itineraryLegsRepository.save(itLeg);
		listOfLegs.add(itLeg);
		
		itineraryReservation.setReservation_status_code(listOfPayments);
		itineraryReservation.setTicket_type_code(listOfLegs);
		itineraryReservation.setTravel_class_code(travelClassCapacity);
		itineraryReservation.setDate_reservation_made(Date.valueOf(LocalDate.now()));
		itineraryReservation.setNumber_in_party(1);
		itineraryReservationsRepository.save(itineraryReservation);
		
		payment.setPayment_status_code(listOfPayments);
		payment.setPayment_date(Date.valueOf(LocalDate.now()));
		payment.setPayment_amount(250.00);
		
		Payments paymentData = paymentsRepository.save(payment);
		assertTrue((paymentData.getPayment_amount() == 250.00) && (paymentData.getPayment_id() > 0));
	}
}
