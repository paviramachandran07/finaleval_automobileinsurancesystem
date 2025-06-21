package com.hexaware.automobileinsurancesystem.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.exceptions.ProposalNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.ProposalRepository;

@Service
public class ProposalServiceImpl implements ProposalService {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private QuoteService quoteService;

    private static final Logger logger = LoggerFactory.getLogger(ProposalServiceImpl.class);

    @Override
    public Proposal createProposal(Proposal proposal) {
        Long userId = proposal.getUser().getUserId();
        Long vehicleId = proposal.getVehicle().getVehicleId();

        logger.info("Creating proposal for user ID: {} and vehicle ID: {}", userId, vehicleId);

        List<Proposal> existingProposals = proposalRepository.findByVehicle_VehicleId(vehicleId);
        boolean alreadyReviewed = existingProposals.stream()
                .anyMatch(p -> p.getStatus() == Proposal.Status.Reviewed);

        if (alreadyReviewed) {
            logger.warn("Attempted to create proposal for a vehicle that already has a reviewed proposal.");
            throw new IllegalStateException("A reviewed proposal already exists for this vehicle.");
        }

        return proposalRepository.save(proposal);
    }

    @Override
    public Proposal updateProposal(Proposal proposal) throws ProposalNotFoundException {
        getProposalById(proposal.getProposalId());
        return proposalRepository.save(proposal);
    }

    @Override
    public void deleteProposal(Long proposalId) throws ProposalNotFoundException {
        Proposal proposal = getProposalById(proposalId);
        proposalRepository.delete(proposal);
    }

    @Override
    public Proposal getProposalById(Long proposalId) throws ProposalNotFoundException {
        logger.info("Fetching proposal with ID: {}", proposalId);
        return proposalRepository.findById(proposalId)
                .orElseThrow(() -> {
                    logger.error("Proposal not found with ID: {}", proposalId);
                    return new ProposalNotFoundException("Proposal not found: " + proposalId);
                });
    }

    @Override
    public List<Proposal> getAllProposals() {
        logger.info("Fetching all proposals");
        return proposalRepository.findAll();
    }

    @Override
    public List<Proposal> getProposalsByUserId(Long userId) {
        logger.info("Fetching proposals for user ID: {}", userId);
        return proposalRepository.findByUser_UserId(userId);
    }

    @Override
    public List<Proposal> getProposalsByVehicleId(Long vehicleId) {
        logger.info("Fetching proposals for vehicle ID: {}", vehicleId);
        return proposalRepository.findByVehicle_VehicleId(vehicleId);
    }

    @Override
    public Proposal reviewProposal(Long proposalId, Proposal.Status status) throws ProposalNotFoundException {
        logger.info("Reviewing proposal ID: {} with status: {}", proposalId, status);
        Proposal proposal = getProposalById(proposalId);
        if (proposal.getVehicle() == null) {
            logger.error("Vehicle is null for proposal ID: {}", proposalId);
            throw new IllegalStateException("Vehicle is missing for proposal ID: " + proposalId);
        }

        proposal.setStatus(status);

        if (status == Proposal.Status.Reviewed) {
            logger.info("Proposal marked as Reviewed. Generating quote...");
            quoteService.createQuoteForProposal(proposal);
            logger.info("Quote generated for proposal ID: {}", proposalId);
        }

        return proposalRepository.save(proposal);
    }

    @Override
    public List<Proposal> getProposalsByUserIdSecure(Long userId, String email) {
        return proposalRepository.findByUser_UserId(userId)
                .stream()
                .filter(p -> p.getUser().getEmail().equals(email))
                .collect(Collectors.toList());
    }
}


