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

import com.hexaware.automobileinsurancesystem.dto.PaymentDTO;
import com.hexaware.automobileinsurancesystem.entities.Payment;
import com.hexaware.automobileinsurancesystem.entities.Quote;
import com.hexaware.automobileinsurancesystem.repositories.QuoteRepository;
import com.hexaware.automobileinsurancesystem.services.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired private PaymentService paymentService;
    @Autowired private QuoteRepository quoteRepository;

    private PaymentDTO toDTO(Payment p) {
        return new PaymentDTO(
            p.getPaymentId(),
            p.getQuote().getQuoteId(),
            p.getPaymentDate(),
            p.getAmountPaid(),
            p.getStatus()
        );
    }

    private Payment toEntity(PaymentDTO dto) {
        Quote quote = quoteRepository.findById(dto.getQuoteId()).orElseThrow();
        return new Payment(
            dto.getPaymentId(),
            quote,
            dto.getPaymentDate(),
            dto.getAmountPaid(),
            dto.getStatus()
        );
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestBody @Valid PaymentDTO dto) {
        logger.info("Creating payment for quoteId={} with amount={}", dto.getQuoteId(), dto.getAmountPaid());
        Payment saved = paymentService.createPayment(toEntity(dto));
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> get(@PathVariable Long id, HttpServletRequest request) {
        logger.info("Fetching payment with ID={}", id);
        Payment payment = paymentService.getPaymentById(id);

        String email = request.getUserPrincipal().getName();
        boolean isOwner = payment.getQuote().getProposal().getUser().getEmail().equals(email);
        boolean isOfficer = request.isUserInRole("OFFICER");

        if (!isOwner && !isOfficer) {
            logger.warn("Unauthorized access to payment ID={} by {}", id, email);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(toDTO(payment));
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @GetMapping
    public List<PaymentDTO> all() {
        logger.info("Fetching all payments");
        return paymentService.getAllPayments()
                             .stream()
                             .map(this::toDTO)
                             .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable Long id, @RequestBody @Valid PaymentDTO dto) {
        logger.info("Updating payment ID={} with amount={}", id, dto.getAmountPaid());
        dto.setPaymentId(id);
        return ResponseEntity.ok(toDTO(paymentService.updatePayment(toEntity(dto))));
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting payment with ID={}", id);
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}



