package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import com.hexaware.automobileinsurancesystem.entities.Vehicle;
import com.hexaware.automobileinsurancesystem.exceptions.UserNotFoundException;
import com.hexaware.automobileinsurancesystem.exceptions.VehicleNotFoundException;

public interface VehicleService {
    Vehicle createVehicle(Vehicle vehicle);
    Vehicle updateVehicle(Vehicle vehicle) throws VehicleNotFoundException;
    void deleteVehicle(Long vehicleId) throws VehicleNotFoundException;
    Vehicle getVehicleById(Long vehicleId) throws VehicleNotFoundException;
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleByRegistrationNumber(String reg) throws VehicleNotFoundException;
    List<Vehicle> getVehiclesByUserId(Long userId) throws UserNotFoundException;
}

