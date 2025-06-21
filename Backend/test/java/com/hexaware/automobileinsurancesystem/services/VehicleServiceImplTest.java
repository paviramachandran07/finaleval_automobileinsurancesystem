package com.hexaware.automobileinsurancesystem.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hexaware.automobileinsurancesystem.entities.Vehicle;
import com.hexaware.automobileinsurancesystem.exceptions.UserNotFoundException;
import com.hexaware.automobileinsurancesystem.exceptions.VehicleNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.UserRepository;
import com.hexaware.automobileinsurancesystem.repositories.VehicleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class VehicleServiceImplTest {

    @Mock private VehicleRepository vehicleRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private VehicleServiceImpl vehicleService;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicle = new Vehicle();
        vehicle.setVehicleId(1L);
        vehicle.setRegistrationNumber("MH12AB1234");
    }

    @Test
    void testCreateVehicle() {
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        Vehicle created = vehicleService.createVehicle(vehicle);
        assertEquals(vehicle, created);
    }

    @Test
    void testGetVehicleById_Success() throws VehicleNotFoundException {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        Vehicle found = vehicleService.getVehicleById(1L);
        assertEquals(vehicle, found);
    }

    @Test
    void testGetVehicleById_NotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(VehicleNotFoundException.class, () -> vehicleService.getVehicleById(1L));
    }

    @Test
    void testGetAllVehicles() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        assertEquals(1, vehicleService.getAllVehicles().size());
    }

    @Test
    void testDeleteVehicleById() throws VehicleNotFoundException {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        vehicleService.deleteVehicle(1L);
        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void testGetVehicleByRegistrationNumber() throws VehicleNotFoundException {
        when(vehicleRepository.findByRegistrationNumber("MH12AB1234")).thenReturn(Optional.of(vehicle));
        assertEquals(vehicle, vehicleService.getVehicleByRegistrationNumber("MH12AB1234"));
    }

    @Test
    void testGetVehiclesByUserId_Success() throws UserNotFoundException {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(vehicleRepository.findByUser_UserId(1L)).thenReturn(List.of(vehicle));
        assertEquals(1, vehicleService.getVehiclesByUserId(1L).size());
    }

    @Test
    void testGetVehiclesByUserId_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> vehicleService.getVehiclesByUserId(1L));
    }
}
