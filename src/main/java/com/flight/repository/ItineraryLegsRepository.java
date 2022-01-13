package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.ItineraryLegs;
import com.flight.id.ItineraryLegsId;

public interface ItineraryLegsRepository extends JpaRepository<ItineraryLegs, ItineraryLegsId>{

}
