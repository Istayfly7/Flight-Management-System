package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.TravelClassCapacity;
import com.flight.id.TravelClassCapacityId;

public interface TravelClassCapacityRepository extends JpaRepository<TravelClassCapacity, TravelClassCapacityId>{

}
