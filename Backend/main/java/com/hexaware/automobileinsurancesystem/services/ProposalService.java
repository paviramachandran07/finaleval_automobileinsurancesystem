package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.exceptions.ProposalNotFoundException;

public interface ProposalService {
    Proposal createProposal(Proposal proposal);
    Proposal updateProposal(Proposal proposal) throws ProposalNotFoundException;
    void deleteProposal(Long proposalId) throws ProposalNotFoundException;
    Proposal getProposalById(Long proposalId) throws ProposalNotFoundException;
    List<Proposal> getAllProposals();
    List<Proposal> getProposalsByUserId(Long userId);
    List<Proposal> getProposalsByVehicleId(Long vehicleId); 
    Proposal reviewProposal(Long proposalId, Proposal.Status status) throws ProposalNotFoundException;
	List<Proposal> getProposalsByUserIdSecure(Long userId, String email); 
}
