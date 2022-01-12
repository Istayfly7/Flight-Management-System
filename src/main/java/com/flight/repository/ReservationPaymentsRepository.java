package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.id.ReservationPaymentsId;
import com.flight.model.ReservationPayments;

public interface ReservationPaymentsRepository extends JpaRepository<ReservationPayments, ReservationPaymentsId>{

}
