package com.hexaware.automobileinsurancesystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.automobileinsurancesystem.entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    List<Vehicle> findByUser_UserId(Long userId);
}
