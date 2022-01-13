package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.FlightCosts;
import com.flight.id.FlightCostsId;

public interface FlightCostsRepository extends JpaRepository<FlightCosts, FlightCostsId>{

}
