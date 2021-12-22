package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.Legs;

public interface LegsRepository extends JpaRepository<Legs, Integer>{

}
