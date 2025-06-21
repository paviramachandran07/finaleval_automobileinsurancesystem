package com.hexaware.automobileinsurancesystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.automobileinsurancesystem.entities.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
	 List<Quote> findByProposal_ProposalId(Long proposalId);
	 List<Quote> findByProposal_User_UserId(Long userId);
}

