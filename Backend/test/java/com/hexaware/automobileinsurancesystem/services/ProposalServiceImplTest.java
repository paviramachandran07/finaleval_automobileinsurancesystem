package com.hexaware.automobileinsurancesystem.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.entities.Vehicle;
import com.hexaware.automobileinsurancesystem.exceptions.ProposalNotFoundException;
import com.hexaware.automobileinsurancesystem.exceptions.UserNotFoundException;
import com.hexaware.automobileinsurancesystem.exceptions.VehicleNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.ProposalRepository;
import com.hexaware.automobileinsurancesystem.repositories.UserRepository;
import com.hexaware.automobileinsurancesystem.repositories.VehicleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProposalServiceImplTest {

    @Mock private ProposalRepository proposalRepository;
    @Mock private UserRepository userRepository;
    @Mock private VehicleRepository vehicleRepository;

    @InjectMocks private ProposalServiceImpl proposalService;

    private Proposal proposal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        proposal = new Proposal();
        proposal.setProposalId(1L);
        proposal.setUser(new User());
        proposal.setVehicle(new Vehicle());
    }

    @Test
    void testCreateProposal() {
        when(proposalRepository.save(proposal)).thenReturn(proposal);
        Proposal saved = proposalService.createProposal(proposal);
        assertEquals(proposal, saved);
    }

    @Test
    void testUpdateProposal() throws ProposalNotFoundException {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        when(proposalRepository.save(proposal)).thenReturn(proposal);
        Proposal updated = proposalService.updateProposal(proposal);
        assertEquals(proposal, updated);
    }

    @Test
    void testDeleteProposalById() throws ProposalNotFoundException {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        proposalService.deleteProposal(1L);
        verify(proposalRepository).delete(proposal);
    }

    @Test
    void testGetProposalById_Success() throws ProposalNotFoundException {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        assertEquals(proposal, proposalService.getProposalById(1L));
    }

    @Test
    void testGetProposalById_NotFound() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProposalNotFoundException.class, () -> proposalService.getProposalById(1L));
    }

    @Test
    void testGetProposalsByUserId_Success() throws UserNotFoundException {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(proposalRepository.findByUser_UserId(1L)).thenReturn(List.of(proposal));
        assertEquals(1, proposalService.getProposalsByUserId(1L).size());
    }

    @Test
    void testGetProposalsByUserId_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> proposalService.getProposalsByUserId(1L));
    }

    @Test
    void testGetProposalsByVehicleId_Success() throws VehicleNotFoundException {
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        when(proposalRepository.findByVehicle_VehicleId(1L)).thenReturn(List.of(proposal));
        assertEquals(1, proposalService.getProposalsByVehicleId(1L).size());
    }

    @Test
    void testGetProposalsByVehicleId_VehicleNotFound() {
        when(vehicleRepository.existsById(1L)).thenReturn(false);
        assertThrows(VehicleNotFoundException.class, () -> proposalService.getProposalsByVehicleId(1L));
    }
}

