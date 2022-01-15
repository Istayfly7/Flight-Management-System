package com.flight.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.Airports;

public interface AirportsRepository extends JpaRepository<Airports, Integer>{
	
	Optional<Airports> findByairportName(String airportName);

}
