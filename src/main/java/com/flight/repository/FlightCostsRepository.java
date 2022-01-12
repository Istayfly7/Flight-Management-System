package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.id.FlightCostsId;
import com.flight.model.FlightCosts;

public interface FlightCostsRepository extends JpaRepository<FlightCosts, FlightCostsId>{

}
