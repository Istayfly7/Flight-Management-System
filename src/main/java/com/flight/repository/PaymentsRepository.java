package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.Payments;

public interface PaymentsRepository extends JpaRepository<Payments, Integer>{

}
