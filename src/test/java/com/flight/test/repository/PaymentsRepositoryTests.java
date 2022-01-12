package com.flight.test.repository;

import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
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

import com.flight.entity.BookingAgents;
import com.flight.entity.CustomerUser;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.Payments;
import com.flight.entity.User;
import com.flight.model.ReservationPayments;
import com.flight.repository.BookingAgentsRepository;
import com.flight.repository.ItineraryReservationsRepository;
import com.flight.repository.PaymentsRepository;
import com.flight.repository.ReservationPaymentsRepository;
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
	
	@Test
	@Rollback(false)
	public void testCreateNewPayment() {
		Payments payment = new Payments();
		
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
		
		ItineraryReservations itineraryReservation = new ItineraryReservations();
		itineraryReservation.setAgent_id(agent);
		itineraryReservation.setPassenger_id(passenger);
		
		List<ReservationPayments> listOfPayments = new ArrayList<>();
		ReservationPayments reservationPayment = new ReservationPayments();
		reservationPayment.setPayment_id(payment.getPayment_id());
		reservationPayment.setReservation_id(itineraryReservation.getReservationId());
		reservationPaymentsRepository.save(reservationPayment);
		listOfPayments.add(reservationPayment);
		
		itineraryReservation.setReservation_status_code(listOfPayments);
		itineraryReservation.setTicket_type_code(ticket_type_code);
		itineraryReservation.setTravel_class_code(travel_class_code);
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
