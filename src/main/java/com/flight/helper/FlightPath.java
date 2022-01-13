package com.flight.helper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import com.flight.calculate.FlightPathCalculate;
import com.flight.entity.Airports;
import com.flight.entity.FlightSchedules;
import com.flight.entity.TravelClassCapacity;

public class FlightPath {
	
	private Airports from;
	private Airports to;
	private LocalDateTime departure; //still need to narrow to one in between dates
	private LocalDateTime arrival;
	int numberInParty; //still need to narrow down by numberin party
	List<FlightSchedules> listOfFlights;
	List<Airports> listOfAirports;
	private int vertices;
	
	public FlightPath(List<FlightSchedules> listOfFlights, List<Airports> listOfAirports, Airports from, Airports to, LocalDateTime departure, LocalDateTime arrival, int numberInParty) {
		this.from = from;
		this.to = to;
		this.departure = departure;
		this.arrival = arrival;
		this.numberInParty = numberInParty;
		this.listOfFlights = listOfFlights;
		this.listOfAirports = listOfAirports;
		this.vertices = listOfAirports.size();
	}
	
	public FlightPathCalculate addAllEdges() {
		FlightPathCalculate flightPathCalculate  = new FlightPathCalculate(this.vertices);
		
		for(FlightSchedules flightSchedule: listOfFlights) {
			
			int from = flightSchedule.getOrigin_airport_code().getAirport_code();
			int to = flightSchedule.getDestination_airport_code().getAirport_code();
			
			flightPathCalculate.addEdge(from, to);
		}
		
		return flightPathCalculate;
	}
	
	public void printAllPathsIds() {
		FlightPathCalculate flightPathCalculate = addAllEdges();
		
		flightPathCalculate.printAllPaths(from.getAirport_code(), to.getAirport_code());
	}
	
	public static void main(String[] args) {
		Airports start = new Airports();
		start.setAirport_code(0);
		start.setAirport_location("39.8561° N, 104.6737° W");
		start.setAirport_name("Denver International Airport");
		
		Airports dest = new Airports();
		dest.setAirport_code(1);
		dest.setAirport_location("25.7969° N, 80.2762° W");
		dest.setAirport_name("Miami Inernational Airport");
		
		Airports dest2 = new Airports();
		dest2.setAirport_code(2);
		dest2.setAirport_location("25.7969");
		dest2.setAirport_name("i Inernational Airport");
		
		TravelClassCapacity travelClassCapacity = new TravelClassCapacity();
		travelClassCapacity.setAircraft_type_code(1);
		
		FlightSchedules flightSchedules = new FlightSchedules();
		flightSchedules.setFlight_number(1);
		
		Timestamp timeStamp = Timestamp.valueOf(LocalDateTime.now());
		flightSchedules.setDeparture_date_time(timeStamp);
		flightSchedules.setArrival_date_time(Timestamp.valueOf(timeStamp.toLocalDateTime().plusHours(2)));
		flightSchedules.setOrigin_airport_code(start);
		flightSchedules.setDestination_airport_code(dest);
		flightSchedules.setUsual_aircraft_type_code(travelClassCapacity);
		
		//still need to test with more than 1 path
		FlightPath flightPath = new FlightPath(Arrays.asList(flightSchedules), Arrays.asList(start, dest2,  dest), start, dest, timeStamp.toLocalDateTime(), timeStamp.toLocalDateTime().plusHours(2), 1);
		flightPath.printAllPathsIds();
	}
	
}
