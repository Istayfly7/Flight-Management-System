package com.flight.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Airports;
import com.flight.entity.BookingAgents;
import com.flight.entity.FlightSchedules;
import com.flight.entity.ItineraryLegs;
import com.flight.entity.ItineraryReservations;
import com.flight.entity.Legs;
import com.flight.entity.Payments;
import com.flight.entity.ReservationPayments;
import com.flight.entity.TravelClassCapacity;
import com.flight.entity.User;
import com.flight.helper.FlightPath;
import com.flight.helper.PrivilegeCheck;
import com.flight.repository.AirportsRepository;
import com.flight.repository.BookingAgentsRepository;
import com.flight.repository.FlightSchedulesRepository;
import com.flight.repository.ItineraryReservationsRepository;
import com.flight.repository.LegsRepository;
import com.flight.repository.PaymentsRepository;
import com.flight.repository.ReservationPaymentsRepository;
import com.flight.repository.UserRepository;

@RestController
@RequestMapping("itinerary_reservations")
public class ItineraryReservationsController extends PrivilegeCheck {

	@Autowired
	private ItineraryReservationsRepository itineraryReservationsRepository;
	
	@Autowired
	private FlightSchedulesRepository flightSchedulesRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LegsRepository legsRepository;
	
	@Autowired
	private BookingAgentsRepository bookingAgentsRepository;

	@Autowired
	private ReservationPaymentsRepository reservationPaymentsRepository;
	
	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private AirportsRepository airportsRepository;

	
	private List<Pair<Pair<Integer, Integer>, FlightSchedules>> finalFlightPaths;
	
	
	@GetMapping("/view/{passenger_id}")
	public ResponseEntity<ItineraryReservations> viewItinerary(@PathVariable("passenger_id") int passenger_id, @RequestParam int reservation_id){
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			Optional<ItineraryReservations> itineraryData = itineraryReservationsRepository.findById(reservation_id);
			
			if(userData.isPresent() && itineraryData.isPresent()) {
				User user = userData.get();
				ItineraryReservations itineraryReservation = itineraryData.get();
				
				if(user.getPassengerId() == itineraryReservation.getPassengerId()) {
					return new ResponseEntity<>(itineraryReservation, HttpStatus.OK);
				}
				else
					throw new Exception("Passenger does not have access to this feature!");
			}
			else {
				if(!userData.isPresent()) 
					throw new Exception("Passenger data not found!");
				else
					throw new Exception("Itinerary data not found!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/create_one_way")
	public ResponseEntity<List<Pair<Double, String>>> createOneWay(@RequestParam int ticketClass, @RequestParam int numberInParty, @RequestParam Date date, @RequestBody Airports from, @RequestBody Airports to) {
		List<Pair<Double, String>> listOfPlans = new ArrayList<>();
		
		try {
			if(flightCheck(date.toLocalDate(), from, to, numberInParty)) {
				List<List<Integer>> flightPlans = getFlightPlanIds(date.toLocalDate(), from, to, numberInParty);
			
				for(List<Integer> flightPlan: flightPlans) {
					List<String> airportNames = convertToAirporNames(flightPlan);
				
					listOfPlans.add(Pair.of(calculateFullFlightPrice(ticketClass, flightPlan), airportNames.toString()));
				}
			
				return new ResponseEntity<>(listOfPlans, HttpStatus.OK);
			}
			else {
				throw new Exception("No Possible flight from " + from.getAirport_name() + " to " + to.getAirport_name() + "!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
		
	
	

	@GetMapping("/create_round_trip/{passenger_id}")
	public ResponseEntity<Pair<Double, List<ItineraryReservations>>> createRoundTrip(@PathVariable("passenger_id") int passenger_id, @RequestParam int ticketClass, @RequestParam int numberInParty, @RequestParam List<Date> dates, @RequestBody Airports from, @RequestBody Airports to) {
		Pair<Double, ItineraryReservations> firstWay = createOneWay(passenger_id, ticketClass, numberInParty, dates.get(0), from, to).getBody();
		Pair<Double, ItineraryReservations> secWay = createOneWay(passenger_id, ticketClass, numberInParty, dates.get(1), to, from).getBody();
		
		try {
			if(firstWay == null || secWay == null)
				throw new Exception("Atleast 1 way returned null!");
			
			double totPrice = firstWay.getFirst() + secWay.getFirst();
			
			Pair<Double, List<ItineraryReservations>> pair = Pair.of(totPrice, Arrays.asList(firstWay.getSecond(), secWay.getSecond()));
			return new ResponseEntity<>(pair, HttpStatus.OK);
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return null;
		}
	}
	
	@GetMapping("/create_multi_city")
	public ResponseEntity<List<Pair<Double, List<Pair<Double, String>>>>> createMultiCity(@RequestParam int ticketClass, @RequestParam int numberInParty, @RequestParam List<Date> dates, @RequestBody List<Airports> from, @RequestBody List<Airports> to) {
		List<Pair<Double, List<Pair<Double, String>>>> listOfPlansPrice = new ArrayList<>();
		List<List<Pair<Double, String>>> listOfPlans = new ArrayList<>();
		
		double totPrice = 0.0;
		
		try {
			for(int i = 0; i < from.size(); i++) {
				listOfPlans.add(createOneWay(ticketClass, numberInParty, dates.get(i), from.get(i), to.get(i)).getBody());
			}
			
			for(int i = 0; i < listOfPlans.size(); i++) {
				for(Pair<Double, String> pair: listOfPlans.get(i)) {
					totPrice += pair.getFirst();
				}
				
				listOfPlansPrice.add(Pair.of(totPrice, listOfPlans.get(i)));
			}
			return new ResponseEntity<>(listOfPlansPrice, HttpStatus.OK);
			
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/save-one-way/{passenger_id}")//finish createlegs
	public ResponseEntity<ItineraryReservations> confirm(@PathVariable("passenger_id") int passenger_id, @RequestParam double paymentAmount, @RequestBody ItineraryReservations itineraryReservations, @RequestBody TravelClassCapacity travelClassCode) {//set flight cost... confirm when payment status is ok and passenger_id match
		try {
			/*Optional<User> userData = userRepository.findById(passenger_id);
			List<BookingAgents> agent = bookingAgentsRepository.findAll();
			Date today = Date.valueOf(LocalDate.now());
			
			if(userData.isPresent()) {
				User u = userData.get();
				if(u.getPassengerId() == itineraryReservations.getPassengerId()) {
					ReservationPayments reservationPayment = new ReservationPayments();
					reservationPayment.setReservation_id(itineraryReservations.getReservationId());
					
					itineraryReservations.setAgent_id(agent.get((int)Math.random() * (agent.size()-1)));
					itineraryReservations.setReservation_status_code(Arrays.asList(reservationPayment));
					itineraryReservations.setTravel_class_code(travelClassCode);
					itineraryReservations.setDate_reservation_made(today);
					
					//create payment object and set it
					Payments payment = new Payments();
					payment.setPayment_amount(paymentAmount);
					payment.setPayment_date(today);
					payment.setPayment_status_code(Arrays.asList(reservationPayment));
					paymentsRepository.save(payment);
					
					reservationPayment.setPayment_id(payment.getPayment_id());
					reservationPaymentsRepository.save(reservationPayment);
					
					
					ItineraryReservations itineraryReservation = itineraryReservationsRepository.save(itineraryReservations);
					return new ResponseEntity<>(itineraryReservation, HttpStatus.OK);
				}
				else{
					throw new Exception("Passenger does not have access to this feature!");
				}
			}
			else {
				throw new Exception("Passenger id not found!");
			}*/
				
			
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/save-round-trip/{passenger_id}")
	public ResponseEntity<List<ItineraryReservations>> confirmRoundTrip(@PathVariable("passenger_id") int passenger_id, @RequestParam double payment, @RequestBody List<ItineraryReservations> itineraryReservations) {//set flight cost... confirm when payment status is ok and passenger_id match
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			
			if(userData.isPresent()) {
				User u = userData.get();
				boolean flag = false;
				List<ItineraryReservations> ir = new ArrayList<>();
				
				for(ItineraryReservations itineraryReservation: itineraryReservations) {
					if(u.getPassengerId() == itineraryReservation.getPassengerId()) {
						itineraryReservation.getReservation_status_code().setPayment_amount(payment);
						
						
						if((itineraryReservation.getLeg_id().get(0).getFlight_Number().getFlight_cost() * itineraryReservation.getNumber_in_party())  - itineraryReservation.getReservation_status_code().getPayment_amount() <= 0) {
							if(flag == true) {
								ir = itineraryReservationsRepository.saveAll(itineraryReservations);
								
							}
							else
								flag = true;
							
						}
						else {
							throw new Exception("Itinerary not confirmed! Passenger did not pay the propper amount!");
						}
					}
					else{
						throw new Exception("Passenger does not have access to this feature!");
					}
				}
				
				return new ResponseEntity<>(ir, HttpStatus.OK);
			}
			else {
				throw new Exception("Passenger id not found!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@DeleteMapping("/remove/{reservation_id}")
	public void removeReservation(@PathVariable("reservation_id") int reservation_id, @RequestParam int passenger_id)
	{
		try {
			Optional<ItineraryReservations> itineraryData = itineraryReservationsRepository.findById(reservation_id);
			
			if(itineraryData.isPresent()) {
				ItineraryReservations itineraryReservation = itineraryData.get();
				if(passenger_id == itineraryReservation.getPassengerId())
					itineraryReservationsRepository.delete(itineraryReservationsRepository.getById(reservation_id));
				else
					throw new Exception("Passenger id does not match the passenger id on the reservation!");
			}
			else {
				throw new Exception("Reservation id not found!");
			}
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
		}
	}
	
	
	private boolean flightCheck(LocalDate date, Airports from, Airports to, int numberInParty) {
		if(!getFlightPlanIds(date, from, to, numberInParty).isEmpty())
			return true;
		
		return false;
	}
	
	
	private List<List<Integer>> getFlightPlanIds(LocalDate date, Airports from, Airports to, int numberInParty){
		//Map<Integer, List<Integer>> planMap = new TreeMap<>();
		
		//1:   use flight path object
		FlightPath flightPath = new FlightPath(flightSchedulesRepository.findAll(), airportsRepository.findAll(), from, to);
		List<List<Integer>> paths = flightPath.getAllPaths();
		
		//1:   narrowDownByNumberinParty();
		paths = narrowDownByNumberinParty(paths, numberInParty, from, to, flightPath);
		
		//2:   narrowDownByDate();
		paths = narrowDownByDate(paths, date, from , to, flightPath);
		
		//create map with paths
		/*for(int i = 0; i < paths.size(); i++) {
			List<Integer> path = paths.get(i);
			
			planMap.put(i, path);
		}*/
		
		finalFlightPaths = flightPath.getFlights();
		return paths;
	}
	
	
	private List<List<Integer>> narrowDownByDate(List<List<Integer>> pathList, LocalDate date, Airports from, Airports to, FlightPath flightPath) {
		List<Pair<Pair<Integer, Integer>, FlightSchedules>> listOfflights = flightPath.getFlights();
		
		//for each path
		for(List<Integer> path: pathList) {
			for(int i = 0; i < path.size(); i++) {
				
				if(path.size() == 2) {
					if(i == 0) {
						for(Pair<Pair<Integer, Integer>, FlightSchedules> flight: listOfflights) {
							if((path.get(i) == flight.getFirst().getFirst()) && (path.get(i+1) == flight.getFirst().getSecond())) {
								if((flight.getSecond().getDeparture_date_time().toLocalDateTime().toLocalDate().getDayOfYear() != date.getDayOfYear()) 
										|| (flight.getSecond().getDeparture_date_time().toLocalDateTime().toLocalDate().getYear()) != date.getYear()) {
									listOfflights.remove(flight);
								}
							}
						}
					}
				}
				else if(path.size() > 2){
					if(i < path.size()-1) {
						for(Pair<Pair<Integer, Integer>, FlightSchedules> flight: listOfflights) {
							if((path.get(i) == flight.getFirst().getFirst()) && (path.get(i+1) == flight.getFirst().getSecond())) {
								if((flight.getSecond().getDeparture_date_time().toLocalDateTime().toLocalDate().getDayOfYear() != date.getDayOfYear()) 
										|| (flight.getSecond().getDeparture_date_time().toLocalDateTime().toLocalDate().getYear()) != date.getYear()) {
									listOfflights.remove(flight);
								}
							}
						}
					}
				}
			}
		}
		
		//turn int List<FlightSchedules>
		List<FlightSchedules> flights = new ArrayList<>();
		for(Pair<Pair<Integer, Integer>, FlightSchedules> flight: listOfflights) {
			flights.add(flight.getSecond());
		}
		
		
		FlightPath newFlightPath = new FlightPath(flights, airportsRepository.findAll(), from, to);
		flightPath.setFlights(newFlightPath.getFlights());
		List<List<Integer>> paths = newFlightPath.getAllPaths();
		
		return paths;
	}

	
	private List<List<Integer>> narrowDownByNumberinParty(List<List<Integer>> pathList, int numberInParty, Airports from, Airports to, FlightPath flightPath) {
		List<Pair<Pair<Integer, Integer>, FlightSchedules>> listOfflights = flightPath.getFlights();
		
		//for each path
		for(List<Integer> path: pathList) {
			for(int i = 0; i < path.size(); i++) {
				
				if(path.size() == 2) {
					if(i == 0) {
						for(Pair<Pair<Integer, Integer>, FlightSchedules> flight: listOfflights) {
							if((path.get(i) == flight.getFirst().getFirst()) && (path.get(i+1) == flight.getFirst().getSecond())) {
								if(flight.getSecond().getUsual_aircraft_type_code().getSeat_capacity() < numberInParty) {
									listOfflights.remove(flight);
								}
							}
						}
					}
				}
				else if(path.size() > 2){
					if(i < path.size()-1) {
						for(Pair<Pair<Integer, Integer>, FlightSchedules> flight: listOfflights) {
							if((path.get(i) == flight.getFirst().getFirst()) && (path.get(i+1) == flight.getFirst().getSecond())) {
								if(flight.getSecond().getUsual_aircraft_type_code().getSeat_capacity() < numberInParty) {
									listOfflights.remove(flight);
								}
							}
						}
					}
				}
			}
		}
		
		List<FlightSchedules> flights = new ArrayList<>();
		for(Pair<Pair<Integer, Integer>, FlightSchedules> flight: listOfflights) {
			flights.add(flight.getSecond());
		}
		
		FlightPath newFlightPath = new FlightPath(flights, airportsRepository.findAll(), from, to);
		flightPath.setFlights(newFlightPath.getFlights());
		List<List<Integer>> paths = newFlightPath.getAllPaths();
		
		return paths;
	}
	
	
	private List<String> convertToAirporNames(List<Integer> flightPlan) throws Exception{
		List<String> flightPlanNames = new ArrayList<>();
		
		for(int airportId: flightPlan) {
			flightPlanNames.add(airportsRepository.findById(airportId).get().getAirport_name());
		}
		
		return flightPlanNames;
	}

	
	private Double calculateFullFlightPrice(int ticketClass, List<Integer> flightPlan) throws Exception{
		Double price = 0.0;
		
		for(int i = 0; i < flightPlan.size(); i++) {
			if(i < flightPlan.size()-1) {
				int fromId = flightPlan.get(i);
				int toId = flightPlan.get(i+1);
				
				for(Pair<Pair<Integer, Integer>, FlightSchedules> flightSchedule: finalFlightPaths) {
					if((flightSchedule.getFirst().getFirst() == fromId) && (flightSchedule.getFirst().getSecond() == toId)) {
						//price += flightSchedule.getSecond().getAirline_code().getFlight_cost();
						if(ticketClass == 1) {
							price += flightSchedule.getSecond().getAirline_code().getFlight_cost() * 1.75;
						}
						else {
							price += flightSchedule.getSecond().getAirline_code().getFlight_cost();
						}
					}
				}
				
			}
		}
		
		
		return price;
	}
	
	/*private boolean flightCheck(Date date, Airports from, Airports to, ItineraryReservations itineraryReservation) {//************************
		List<FlightSchedules> listOfFlights = flightSchedulesRepository.findAll();
		
		try {
			for(int i = 0; i < listOfFlights.size(); i++) {
				if(Date.valueOf(listOfFlights.get(i).getDeparture_date_time().toLocalDateTime().toLocalDate()) == date && listOfFlights.get(i).getOrigin_airport_code() == from
						&& listOfFlights.get(i).getDestination_airport_code() == to) {
					/*List<Legs> legs = itineraryReservation.getLeg_id();
					List<ItineraryLegs> itineraryLegs = itineraryReservation.getTicket_type_code();
					List<Legs> listOfLegs = legsRepository.findAll();
					
					for(Legs leg: listOfLegs)//saving legs for itinerary
						if(leg.getFlight_Number().getFlight_number() == listOfFlights.get(i).getFlight_number()) {
							legs.add(leg);
							itineraryReservationsRepository.save(itineraryReservation);
						}*//*
							
					return true;
				}
				else if(Date.valueOf(listOfFlights.get(i).getDeparture_date_time().toLocalDateTime().toLocalDate()) == date && listOfFlights.get(i).getOrigin_airport_code() == from) {
					
					return flightCheck(date, listOfFlights.get(++i).getOrigin_airport_code(), to, itineraryReservation);
				}
			}
			
			return false;
		}
		catch(Exception ex) {
			return false;
		}
	}*/

	/*private ItineraryReservations flightCheck(Date date, Airports from, Airports to, int numberInParty) {//************************
	List<FlightSchedules> listOfFlights = flightSchedulesRepository.findAll();
	
	try {
		for(int i = 0; i < listOfFlights.size(); i++) {
			ItineraryReservations itineraryReservation = new ItineraryReservations();
			itineraryReservation.setNumber_in_party(numberInParty);
			itineraryReservationsRepository.save(itineraryReservation);
			
			ItineraryLegs itLeg = new ItineraryLegs();
			itLeg.setReservation_id(itineraryReservation.getReservationId());
			
			if(Date.valueOf(listOfFlights.get(i).getDeparture_date_time().toLocalDateTime().toLocalDate()) == date && listOfFlights.get(i).getOrigin_airport_code() == from
					&& listOfFlights.get(i).getDestination_airport_code() == to) {
				//create last leg in list of leg add leg to reservation
				//from the solo intineraryleg(add to list of interarylegs)
				//make sure there is enough seats
				Legs leg = new Legs(listOfFlights.get(i));
				legsRepository.save(leg);
				itLeg.setLeg_id(new ArrayList<>(leg.getLeg_id()));
				
				return itineraryReservation;
			}
			else if(Date.valueOf(listOfFlights.get(i).getDeparture_date_time().toLocalDateTime().toLocalDate()) == date && listOfFlights.get(i).getOrigin_airport_code() == from) {
				//create only 1 itinerayleg add it to list of itineraryleg
				//with a list of legs per itineraryLeg and make sure there is enough seats
				return flightCheck(date, listOfFlights.get(++i).getOrigin_airport_code(), to, numberInParty);
			}
		}
		
		return null;
	}
	catch(Exception ex) {
		return null;
	}
}*/

}
