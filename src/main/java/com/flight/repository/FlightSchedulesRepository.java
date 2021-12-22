package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.FlightSchedules;

public interface FlightSchedulesRepository extends JpaRepository<FlightSchedules, Integer>{

}
