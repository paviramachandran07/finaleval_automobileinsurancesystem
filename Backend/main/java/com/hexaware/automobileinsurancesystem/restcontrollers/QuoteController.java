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

import com.hexaware.automobileinsurancesystem.dto.QuoteDTO;
import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.entities.Quote;
import com.hexaware.automobileinsurancesystem.repositories.ProposalRepository;
import com.hexaware.automobileinsurancesystem.services.QuoteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quotes")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class QuoteController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

    @Autowired private QuoteService quoteService;
    @Autowired private ProposalRepository proposalRepository;

    private QuoteDTO toDTO(Quote q) {
        return new QuoteDTO(
            q.getQuoteId(),
            q.getProposal().getProposalId(),
            q.getPremiumAmount(),
            q.getStatus(),
            q.getCoverageDetails()
        );
    }

    private Quote toEntity(QuoteDTO dto) {
        Proposal proposal = proposalRepository.findById(dto.getProposalId())
                .orElseThrow(() -> new RuntimeException("Proposal not found with ID: " + dto.getProposalId()));
        return new Quote(
            dto.getQuoteId(),
            proposal,
            dto.getPremiumAmount(),
            dto.getStatus(),
            dto.getCoverageDetails()
        );
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PostMapping
    public ResponseEntity<QuoteDTO> create(@RequestBody @Valid QuoteDTO dto) {
        logger.info("Creating quote for proposal ID: {}", dto.getProposalId());
        QuoteDTO createdQuote = toDTO(quoteService.createQuote(toEntity(dto)));
        logger.info("Quote created with ID: {}", createdQuote.getQuoteId());
        return new ResponseEntity<>(createdQuote, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping("/{id}")
    public ResponseEntity<QuoteDTO> get(@PathVariable Long id) {
        logger.info("Fetching quote with ID: {}", id);
        QuoteDTO quote = toDTO(quoteService.getQuoteById(id));
        logger.info("Quote fetched: {}", quote.getQuoteId());
        return ResponseEntity.ok(quote);
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @GetMapping
    public List<QuoteDTO> all() {
        logger.info("Fetching all quotes");
        return quoteService.getAllQuotes().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PutMapping("/{id}")
    public ResponseEntity<QuoteDTO> update(@PathVariable Long id, @RequestBody @Valid QuoteDTO dto) {
        logger.info("Updating quote with ID: {}", id);
        dto.setQuoteId(id);
        QuoteDTO updatedQuote = toDTO(quoteService.updateQuote(toEntity(dto)));
        logger.info("Quote updated: {}", updatedQuote.getQuoteId());
        return ResponseEntity.ok(updatedQuote);
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting quote with ID: {}", id);
        quoteService.deleteQuote(id);
        logger.info("Quote deleted: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping(params = "proposalId")
    public ResponseEntity<List<QuoteDTO>> getQuotesByProposalId(
            @RequestParam Long proposalId,
            HttpServletRequest request) {

        logger.info("Fetching quotes for proposalId: {}", proposalId);

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found with ID: " + proposalId));

        String email = request.getUserPrincipal().getName();

        boolean isOwner = proposal.getUser().getEmail().equals(email);
        boolean isOfficer = request.isUserInRole("OFFICER");

        if (!isOwner && !isOfficer) {
            logger.warn("Unauthorized access to quotes of proposalId={} by {}", proposalId, email);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<QuoteDTO> quotes = quoteService.getQuotesByProposalId(proposalId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(quotes);
    }
}





