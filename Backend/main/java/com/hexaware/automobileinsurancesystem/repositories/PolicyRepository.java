package com.hexaware.automobileinsurancesystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.automobileinsurancesystem.entities.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
   
	List<Policy> findByPayment_PaymentId(Long paymentId); 
}

