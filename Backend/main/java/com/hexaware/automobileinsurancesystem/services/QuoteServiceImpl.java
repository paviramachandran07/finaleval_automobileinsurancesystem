package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.entities.Quote;
import com.hexaware.automobileinsurancesystem.exceptions.QuoteNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.QuoteRepository;

@Service
public class QuoteServiceImpl implements QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;

    private static final Logger logger = LoggerFactory.getLogger(QuoteServiceImpl.class);

    @Override
    public Quote createQuote(Quote quote) {
        logger.info("Creating quote with ID: {}", quote.getQuoteId());
        return quoteRepository.save(quote);
    }

    @Override
    public Quote createQuoteForProposal(Proposal proposal) {
        logger.info("Creating quote for proposal ID: {}", proposal.getProposalId());
        if (proposal == null || proposal.getVehicle() == null) {
            logger.error("Proposal or vehicle is null for proposal ID: {}", 
                proposal != null ? proposal.getProposalId() : "null");
            throw new IllegalArgumentException("Proposal or vehicle cannot be null");
        }
        Quote quote = new Quote();
        quote.setProposal(proposal);
        double premium = calculatePremium(proposal);
        quote.setPremiumAmount(premium);
        quote.setCoverageDetails("Basic coverage for vehicle " + proposal.getVehicle().getRegistrationNumber());
        Quote savedQuote = quoteRepository.save(quote);
        logger.info("Quote created with ID: {} for proposal ID: {}", savedQuote.getQuoteId(), proposal.getProposalId());
        return savedQuote;
    }

    @Override
    public Quote updateQuote(Quote quote) throws QuoteNotFoundException {
        getQuoteById(quote.getQuoteId());
        return quoteRepository.save(quote);
    }

    @Override
    public void deleteQuote(Long quoteId) throws QuoteNotFoundException {
        Quote quote = getQuoteById(quoteId);
        quoteRepository.delete(quote);
    }

    @Override
    public Quote getQuoteById(Long quoteId) throws QuoteNotFoundException {
        return quoteRepository.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + quoteId));
    }

    @Override
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Override
    public List<Quote> getQuotesByProposalId(Long proposalId) {
        return quoteRepository.findByProposal_ProposalId(proposalId);
    }

    private double calculatePremium(Proposal proposal) {
        int vehicleAge = 2025 - proposal.getVehicle().getYearOfManufacture();
        double premium = 1000.0 + (10.0 * vehicleAge); // Base $1000 + $10 per year
        logger.info("Calculated premium for proposal ID: {}: ${}", proposal.getProposalId(), premium);
        return premium;
    }
}
