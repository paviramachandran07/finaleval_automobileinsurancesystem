package com.hexaware.automobileinsurancesystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.automobileinsurancesystem.entities.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

	List<Proposal> findByUser_UserId(Long userId);
    List<Proposal> findByVehicle_VehicleId(Long vehicleId);
    //List<Proposal> findByVehicle_VehicleIdAndUser_UserId(Long vehicleId, Long userId);
    
}
