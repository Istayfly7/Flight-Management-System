package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.Airports;

public interface AirportsRepository extends JpaRepository<Airports, Integer>{

}
