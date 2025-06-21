
package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.entities.Vehicle;
import com.hexaware.automobileinsurancesystem.exceptions.UserNotFoundException;
import com.hexaware.automobileinsurancesystem.exceptions.VehicleNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.UserRepository;
import com.hexaware.automobileinsurancesystem.repositories.VehicleRepository;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        Long userId = vehicle.getUser().getUserId();

        User fullUser = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        vehicle.setUser(fullUser);

        logger.info("Creating vehicle with reg: {}", vehicle.getRegistrationNumber());
        return vehicleRepository.save(vehicle);
    }


    @Override
    public Vehicle updateVehicle(Vehicle vehicle) throws VehicleNotFoundException {
        getVehicleById(vehicle.getVehicleId());
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteVehicle(Long vehicleId) throws VehicleNotFoundException {
        Vehicle vehicle = getVehicleById(vehicleId);
        vehicleRepository.delete(vehicle);
    }

    @Override
    public Vehicle getVehicleById(Long vehicleId) throws VehicleNotFoundException {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found: " + vehicleId));
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleByRegistrationNumber(String reg) throws VehicleNotFoundException {
        return vehicleRepository.findByRegistrationNumber(reg)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found: " + reg));
    }

    @Override
    public List<Vehicle> getVehiclesByUserId(Long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found: " + userId);
        }
        return vehicleRepository.findByUser_UserId(userId);
    }
}
