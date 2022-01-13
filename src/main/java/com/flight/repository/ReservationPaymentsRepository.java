package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.ReservationPayments;
import com.flight.id.ReservationPaymentsId;

public interface ReservationPaymentsRepository extends JpaRepository<ReservationPayments, ReservationPaymentsId>{

}
