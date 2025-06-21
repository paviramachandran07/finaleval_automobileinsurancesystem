package com.hexaware.automobileinsurancesystem.restcontrollers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hexaware.automobileinsurancesystem.dto.VehicleDTO;
import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.entities.Vehicle;
import com.hexaware.automobileinsurancesystem.repositories.UserRepository;
import com.hexaware.automobileinsurancesystem.services.VehicleService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserRepository userRepository;

    private VehicleDTO toDTO(Vehicle v) {
        return new VehicleDTO(v.getVehicleId(), v.getUser().getUserId(), v.getType(),
                v.getModel(), v.getRegistrationNumber(), v.getYearOfManufacture());
    }

    private Vehicle toEntity(VehicleDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        return new Vehicle(dto.getVehicleId(), user, dto.getType(), dto.getModel(),
                dto.getRegistrationNumber(), dto.getYearOfManufacture());
    }

    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public ResponseEntity<VehicleDTO> create(@RequestBody @Valid VehicleDTO dto) {
        logger.info("Creating vehicle for user ID: {}", dto.getUserId());
        Vehicle created = vehicleService.createVehicle(toEntity(dto));
        return new ResponseEntity<>(toDTO(created), HttpStatus.CREATED);
    }

   
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> get(@PathVariable Long id) {
        logger.info("Fetching vehicle with ID: {}", id);
        return ResponseEntity.ok(toDTO(vehicleService.getVehicleById(id)));
    }

   
    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @GetMapping
    public List<VehicleDTO> all() {
        logger.info("Fetching all vehicles");
        return vehicleService.getAllVehicles().stream().map(this::toDTO).collect(Collectors.toList());
    }

  
    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable Long id, @RequestBody @Valid VehicleDTO dto) {
        logger.info("Updating vehicle with ID: {}", id);
        dto.setVehicleId(id);
        Vehicle updated = vehicleService.updateVehicle(toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    
    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting vehicle with ID: {}", id);
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}


