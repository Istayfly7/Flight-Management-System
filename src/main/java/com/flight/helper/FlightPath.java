package com.flight.helper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.util.Pair;

import com.flight.calculate.FlightPathCalculate;
import com.flight.entity.Airports;
import com.flight.entity.FlightSchedules;
import com.flight.entity.TravelClassCapacity;

public class FlightPath {
	
	private Airports from;
	private Airports to;
	List<FlightSchedules> listOfFlights;
	List<Airports> listOfAirports;
	private int vertices;
	private List<Pair<Pair<Integer, Integer>, FlightSchedules>> flights;
	
	public FlightPath(List<FlightSchedules> listOfFlights, List<Airports> listOfAirports, Airports from, Airports to) {
		this.from = from;
		this.to = to;
		this.listOfFlights = listOfFlights;
		this.listOfAirports = listOfAirports;
		this.vertices = listOfAirports.size();
		this.flights = new ArrayList<>();
	}
	
	public FlightPathCalculate addAllEdges() {
		FlightPathCalculate flightPathCalculate  = new FlightPathCalculate(this.vertices);
		
		for(FlightSchedules flightSchedule: listOfFlights) {
			
			int from = flightSchedule.getOrigin_airport_code().getAirport_code()-1;
			int to = flightSchedule.getDestination_airport_code().getAirport_code()-1;
			
			//System.out.println("flight: " + flightSchedule.getFlight_number());
			//System.out.println("edge: " + from + "-" + to);
			
			flightPathCalculate.addEdge(from, to);
			flights.add(Pair.of(Pair.of(from, to), flightSchedule));
		}
		
		return flightPathCalculate;
	}
	
	public List<List<Integer>> getAllPaths() {
		FlightPathCalculate flightPathCalculate = addAllEdges();
		
		flightPathCalculate.printAllPaths(from.getAirport_code()-1, to.getAirport_code()-1);
		
		
		return flightPathCalculate.parsePathString(flightPathCalculate.getPathString());
	}
	
	public List<Pair<Pair<Integer, Integer>, FlightSchedules>> getFlights(){
		return this.flights;
	}
	
	public void setFlights(List<Pair<Pair<Integer, Integer>, FlightSchedules>> flights){
		this.flights = flights;
	}
	

	public static void main(String[] args) {
		//latitude, longitude
		Airports start = new Airports();
		start.setAirport_code(1);
		start.setAirport_location("39.8561, 104.6737");
		start.setAirport_name("Denver International Airport");
		
		Airports dest = new Airports();
		dest.setAirport_code(2);
		dest.setAirport_location("25.7969, 80.2762");
		dest.setAirport_name("Miami Inernational Airport");
		
		Airports mid = new Airports();
		mid.setAirport_code(3);
		mid.setAirport_location("25.7969,85.2762");
		mid.setAirport_name("i Inernational Airport");
		
		TravelClassCapacity travelClassCapacity1 = new TravelClassCapacity();
		travelClassCapacity1.setAircraft_type_code(1);
		
		TravelClassCapacity travelClassCapacity2 = new TravelClassCapacity();
		travelClassCapacity2.setAircraft_type_code(2);
		
		TravelClassCapacity travelClassCapacity3 = new TravelClassCapacity();
		travelClassCapacity3.setAircraft_type_code(3);
		
		FlightSchedules flightSchedules1 = new FlightSchedules();
		flightSchedules1.setFlight_number(1);
		
		Timestamp timeStamp = Timestamp.valueOf(LocalDateTime.now());
		flightSchedules1.setDeparture_date_time(timeStamp);
		flightSchedules1.setArrival_date_time(Timestamp.valueOf(timeStamp.toLocalDateTime().plusHours(2)));
		flightSchedules1.setOrigin_airport_code(start);
		flightSchedules1.setDestination_airport_code(dest);
		flightSchedules1.setUsual_aircraft_type_code(travelClassCapacity1);
		
		FlightSchedules flightSchedules2 = new FlightSchedules();
		flightSchedules2.setFlight_number(2);
		
		flightSchedules2.setDeparture_date_time(timeStamp);
		flightSchedules2.setArrival_date_time(Timestamp.valueOf(timeStamp.toLocalDateTime().plusHours(2)));
		flightSchedules2.setOrigin_airport_code(start);
		flightSchedules2.setDestination_airport_code(mid);
		flightSchedules2.setUsual_aircraft_type_code(travelClassCapacity2);
		
		FlightSchedules flightSchedules3 = new FlightSchedules();
		flightSchedules3.setFlight_number(3);
		
		flightSchedules3.setDeparture_date_time(timeStamp);
		flightSchedules3.setArrival_date_time(Timestamp.valueOf(timeStamp.toLocalDateTime().plusHours(2)));
		flightSchedules3.setOrigin_airport_code(mid);
		flightSchedules3.setDestination_airport_code(dest);
		flightSchedules3.setUsual_aircraft_type_code(travelClassCapacity3);
		
		//still need to test with more than 1 path
		FlightPath flightPath = new FlightPath(Arrays.asList(flightSchedules1, flightSchedules2, flightSchedules3), Arrays.asList(start, mid,  dest), start, dest);
		
		
		List<List<Integer>> paths = flightPath.getAllPaths();
		
		
		for(List<Integer> path: paths) {
			//List<Integer> path = paths.get(i);
			
			System.out.println(path);
		}
	}
	
}
