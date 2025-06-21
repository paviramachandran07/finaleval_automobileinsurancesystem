package com.hexaware.automobileinsurancesystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.automobileinsurancesystem.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByQuote_QuoteId(Long quoteId);
   

}

