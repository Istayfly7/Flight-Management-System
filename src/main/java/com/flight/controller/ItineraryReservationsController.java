package com.flight.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.flight.repository.ItineraryLegsRepository;
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

	@Autowired
	private ItineraryLegsRepository itineraryLegsRepository;
	
	//set by confirm a flight type. like one way or round trip {not confirm payment}
	
	private List<Pair<Pair<Integer, Integer>, FlightSchedules>> finalFlightPaths;
	
	private List<Integer> chosenPath;
	
	private int ticketClass;
	
	private int numberInParty;
	
	
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
	
	
	@GetMapping("/create-one-way")
	public ResponseEntity<List<Pair<Double, /*List*/String>>> createOneWay(@RequestParam(required=true) int ticketClass, @RequestParam(required=true) int numberInParty, @RequestParam(required=true) Date date, @RequestParam int fromId, @RequestParam int toId) {
		List<Pair<Double, String>> listOfPlans = new ArrayList<>();
		Optional<Airports> fromData = airportsRepository.findById(fromId);
		Optional<Airports> toData = airportsRepository.findById(toId);
		
		try {
			if(fromData.isPresent() && toData.isPresent()) {
				Airports from = fromData.get();
				Airports to = toData.get();
				
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
			
			throw new Exception("no data found for the from or to airport id given");
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex.getMessage());
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
		
	
	@GetMapping("/create_multi_ways")
	public ResponseEntity<List<Pair<Double, List<Pair<Double, String>>>>> createMultiWays(@RequestParam int ticketClass, @RequestParam int numberInParty, @RequestParam List<Date> dates, @RequestBody List<Airports> from, @RequestBody List<Airports> to) {
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
	
	
	@PostMapping("/save-one-way/{passenger_id}")
	public ResponseEntity<Pair<Payments, ItineraryReservations>> confirm(@PathVariable("passenger_id") int passenger_id, 
			@RequestParam double paymentAmount, @RequestParam int ticketClass, @RequestParam int numberInParty, 
			@RequestBody List<String> chosenPathstr) {
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			List<BookingAgents> agents = bookingAgentsRepository.findAll();
			Date dateReservationMade = Date.valueOf(LocalDate.now());
			ItineraryReservations itineraryReservation = new ItineraryReservations();
			
			itineraryReservation.setDate_reservation_made(dateReservationMade);
			itineraryReservation.setNumber_in_party(numberInParty);
			
			if(userData.isPresent()) {
				User u = userData.get();
				itineraryReservation.setPassenger_id(u);
				
				BookingAgents agent = agents.get((int) ((Math.random() * agents.size()) - 1));
				itineraryReservation.setAgent_id(agent);
				ItineraryReservations it = itineraryReservationsRepository.save(itineraryReservation);
				
				Payments payment = new Payments();
				payment.setPayment_amount(paymentAmount);
				payment.setPayment_date(dateReservationMade);
				
				Payments p = paymentsRepository.save(payment);
				
				//turn into list of airport ids
				List<Integer> chosenPath = new ArrayList<>();
				for(String airportName: chosenPathstr) {
					chosenPath.add(airportsRepository.findByairportName(airportName).get().getAirport_code());
				}
				
				this.chosenPath = chosenPath;
				this.numberInParty = numberInParty;
				this.ticketClass = ticketClass;
				
				return new ResponseEntity<>(Pair.of(p, it), HttpStatus.OK);
			}
			else {
				throw new Exception("Passenger id not found!");
			}
			
		}
		catch(Exception ex) {
			System.out.println(ex.fillInStackTrace());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/save-multi-ways/{passenger_id}")
	public ResponseEntity<List<ItineraryReservations>> confirmMultiWays(@PathVariable("passenger_id") int passenger_id,
			@RequestParam double paymentAmount, @RequestParam int ticketClass, @RequestParam int numberInParty,
			@RequestParam List<Date> dates, @RequestBody List<Airports> from, @RequestBody List<Airports> to, @RequestBody List<List<String>> chosenPathstr) {
		try {
			Optional<User> userData = userRepository.findById(passenger_id);
			
			if(userData.isPresent()) {
				//User u = userData.get();
				List<ItineraryReservations> ItineraryReservations = new ArrayList<>();
				
				for(int i = 0; i < from.size(); i++) {
					ItineraryReservations.add(confirm(passenger_id, paymentAmount, ticketClass, numberInParty,
							dates.get(i), from.get(i), to.get(i), chosenPathstr.get(i)).getBody());
				}
				
				return new ResponseEntity<>(ItineraryReservations, HttpStatus.OK);
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
	
	
	@PostMapping("/confirm-payment")
	public ResponseEntity<Payments> confirmPayment(@RequestParam(required=true) int reservation_id, @RequestParam(required=true) int payment_id){
		Optional<ItineraryReservations> itineraryReservationData = itineraryReservationsRepository.findById(reservation_id);
		//link interanray legs and check itinerary reseravation reservationpayment
		try {
			if(itineraryReservationData.isPresent()) {
				ReservationPayments reservationPayments = new ReservationPayments();
				reservationPayments.setReservation_id(reservation_id);
				reservationPayments.setPayment_id(payment_id);
				reservationPaymentsRepository.save(reservationPayments);
				
				//create itinerary legs list
				ItineraryLegs itineraryLegs = new ItineraryLegs();
				itineraryLegs.setReservation_id(reservation_id);
				
				List<TravelClassCapacity> listOfPlanes = new ArrayList<>();
				List<Integer> listOfLegs = new ArrayList<>();
				for(int i = 0; i < this.chosenPath.size() - 1; i++) {
					for(Pair<Pair<Integer, Integer>, FlightSchedules> flightPath: this.finalFlightPaths) {
						if((this.chosenPath.get(i) == flightPath.getFirst().getFirst() + 1) && (this.chosenPath.get(i+1) == flightPath.getFirst().getSecond() + 1)) {
							for(Legs leg: legsRepository.findAll()) {
								if(leg.getFlight_Number().getFlight_number() == flightPath.getSecond().getFlight_number()) {
									listOfLegs.add(flightPath.getSecond().getFlight_number());
									flightPath.getSecond().getUsual_aircraft_type_code().reserveSeats(this.numberInParty, this.ticketClass);
									listOfPlanes.add(flightPath.getSecond().getUsual_aircraft_type_code());
								}
							}
						}
					}
				}
				itineraryLegs.setLeg_id(listOfLegs);
				
				ItineraryReservations itineraryReservation = itineraryReservationData.get();
				itineraryReservation.setTravel_class_code(listOfPlanes);
				itineraryReservation.setTicket_type_code(Arrays.asList(itineraryLegs));
				
				//itineraryReservationsRepository.save(itineraryReservation);
				itineraryLegsRepository.save(itineraryLegs);
				
				
				
				Optional<Payments> paymentData = paymentsRepository.findById(payment_id);
				
				if(paymentData.isPresent()) {
					Payments payment = paymentData.get();
					
					return new ResponseEntity<>(payment, HttpStatus.OK);
				}
				
				throw new Exception("No data found for payment id " + payment_id + "!"); 
			}
			
			throw new Exception("No data found for reservation id " + reservation_id + "!");
		}
		catch(Exception ex) {
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
				//remove everything related to the itinerary ressrvation
				//payments, reseravationpayments, itneraryLegs
				//add seats back
				
				ItineraryReservations itineraryReservation = itineraryData.get();
				
				for(ReservationPayments reservationPayments: itineraryReservation.getReservation_status_code()) {
					paymentsRepository.deleteById(reservationPayments.getPayment_id());
					reservationPaymentsRepository.delete(reservationPayments);
				}
				
				for(ItineraryLegs itLeg: itineraryReservation.getTicket_type_code())
					itineraryLegsRepository.delete(itLeg);
				
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
		
		this.finalFlightPaths = flightPath.getFlights();
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
					if((flightSchedule.getFirst().getFirst()+1 == fromId) && (flightSchedule.getFirst().getSecond()+1 == toId)) {
						//price += flightSchedule.getSecond().getAirline_code().getFlight_cost();
						if(ticketClass == 1) {
							price += Double.parseDouble(String.format("%.2f", flightSchedule.getSecond().getFlightCost() * 1.75));
						}
						else {
							price += flightSchedule.getSecond().getFlightCost();
						}
					}
				}
				
			}
		}
		
		
		return price;
	}
	

}
