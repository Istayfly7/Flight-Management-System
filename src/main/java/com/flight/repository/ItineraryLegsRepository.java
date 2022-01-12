package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.id.ItineraryLegsId;
import com.flight.model.ItineraryLegs;

public interface ItineraryLegsRepository extends JpaRepository<ItineraryLegs, ItineraryLegsId>{

}
