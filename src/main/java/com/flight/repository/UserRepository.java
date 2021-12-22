package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
