package com.hexaware.automobileinsurancesystem.restcontrollers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hexaware.automobileinsurancesystem.dto.ProposalDTO;
import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.entities.Vehicle;
import com.hexaware.automobileinsurancesystem.repositories.UserRepository;
import com.hexaware.automobileinsurancesystem.repositories.VehicleRepository;
import com.hexaware.automobileinsurancesystem.services.ProposalService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/proposals")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProposalController {

    private static final Logger logger = LoggerFactory.getLogger(ProposalController.class);

    @Autowired private ProposalService proposalService;
    @Autowired private UserRepository userRepository;
    @Autowired private VehicleRepository vehicleRepository;

    private ProposalDTO toDTO(Proposal p) {
        return new ProposalDTO(
                p.getProposalId(),
                p.getUser().getUserId(),
                p.getVehicle().getVehicleId(),
                p.getStatus(),
                p.getCreatedAt()
        );
    }

    private Proposal toEntity(ProposalDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId()).orElseThrow();
        return new Proposal(
                dto.getProposalId(),
                user,
                vehicle,
                dto.getStatus(),
                dto.getCreatedAt()
        );
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public ResponseEntity<ProposalDTO> create(@RequestBody @Valid ProposalDTO dto) {
        logger.info("Creating proposal for userId={} and vehicleId={}", dto.getUserId(), dto.getVehicleId());
        Proposal saved = proposalService.createProposal(toEntity(dto));
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProposalDTO> get(@PathVariable Long id) {
        logger.info("Fetching proposal with ID={}", id);
        Proposal proposal = proposalService.getProposalById(id);
        return ResponseEntity.ok(toDTO(proposal));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping(params = "userId")
    public List<ProposalDTO> getProposalsByUserId(@RequestParam Long userId, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();

        if (request.isUserInRole("ROLE_OFFICER")) {
            // Officer can see all proposals of any user
            return proposalService.getProposalsByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
        } else {
            // Users can only access their own proposals
            return proposalService.getProposalsByUserIdSecure(userId, email).stream().map(this::toDTO).collect(Collectors.toList());
        }
    }


    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @GetMapping
    public List<ProposalDTO> all() {
        logger.info("Fetching all proposals");
        return proposalService.getAllProposals().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProposalDTO> update(@PathVariable Long id, @RequestBody @Valid ProposalDTO dto) {
        logger.info("Updating proposal ID={} by userId={}", id, dto.getUserId());
        dto.setProposalId(id);
        Proposal updated = proposalService.updateProposal(toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PutMapping("/{id}/review")
    public ResponseEntity<ProposalDTO> reviewProposal(@PathVariable Long id, @RequestBody ProposalDTO dto) {
        logger.info("Officer reviewing proposal ID: {}", id);
        Proposal updated = proposalService.reviewProposal(id, dto.getStatus());
        return ResponseEntity.ok(toDTO(updated));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting proposal with ID={}", id);
        proposalService.deleteProposal(id);
        return ResponseEntity.noContent().build();
    }
}

