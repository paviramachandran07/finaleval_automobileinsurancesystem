package com.hexaware.automobileinsurancesystem.restcontrollers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hexaware.automobileinsurancesystem.dto.PolicyDTO;
import com.hexaware.automobileinsurancesystem.entities.Payment;
import com.hexaware.automobileinsurancesystem.entities.Policy;
import com.hexaware.automobileinsurancesystem.exceptions.PolicyNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.PaymentRepository;
import com.hexaware.automobileinsurancesystem.services.PolicyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PolicyController {

    @Autowired private PolicyService policyService;
    @Autowired private PaymentRepository paymentRepository;

    private PolicyDTO toDTO(Policy p) {
        return new PolicyDTO(
                p.getPolicyId(),
                p.getPayment().getPaymentId(),
                p.getPolicyNumber(),
                p.getIssuedDate(),
                p.getExpiryDate(),
                p.getStatus(),
                p.getDocumentPath(),
                p.getTerms()
        );
    }

    private Policy toEntity(PolicyDTO dto) {
        Payment payment = paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found: " + dto.getPaymentId()));
        return new Policy(
                dto.getPolicyId(),
                payment,
                dto.getPolicyNumber(),
                dto.getIssuedDate(),
                dto.getExpiryDate(),
                dto.getStatus(),
                dto.getDocumentPath(),
                dto.getTerms()
        );
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PostMapping
    public ResponseEntity<PolicyDTO> create(@RequestBody @Valid PolicyDTO dto) {
        return new ResponseEntity<>(toDTO(policyService.createPolicy(toEntity(dto))), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping("/{id}")
    public ResponseEntity<PolicyDTO> get(@PathVariable Long id, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        Policy policy = policyService.getPolicyByIdSecure(id, email);
        return ResponseEntity.ok(toDTO(policy));
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @GetMapping
    public List<PolicyDTO> all() {
        return policyService.getAllPolicies()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(params = "userId")
    public List<PolicyDTO> getPoliciesByUser(@RequestParam Long userId) {
        return policyService.getPoliciesByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PutMapping("/{id}")
    public ResponseEntity<PolicyDTO> update(@PathVariable Long id, @RequestBody @Valid PolicyDTO dto) {
        dto.setPolicyId(id);
        return ResponseEntity.ok(toDTO(policyService.updatePolicy(toEntity(dto))));
    }

    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping("/number/{policyNumber}")
    public ResponseEntity<PolicyDTO> getByNumber(@PathVariable String policyNumber) {
        return ResponseEntity.ok(toDTO(policyService.getPolicyByNumber(policyNumber)));
    }

    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/quote/{quoteId}")
    public ResponseEntity<PolicyDTO> getPolicyByQuoteId(@PathVariable Long quoteId) {
        Policy policy = policyService.getPolicyByQuoteId(quoteId);
        return ResponseEntity.ok(toDTO(policy));
    }

    
    
    
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_OFFICER')")
    @GetMapping("/download/{policyId}")
    public ResponseEntity<Resource> downloadPolicy(@PathVariable Long policyId, HttpServletRequest request) throws IOException {
        String email = request.getUserPrincipal().getName(); // Get logged-in email
        Policy policy = policyService.getPolicyByIdSecure(policyId, email); // ensure this policy belongs to this user or officer

        String fileContent = "Policy Details:\n"
                + "Policy ID: " + policy.getPolicyId() + "\n"
                + "Policy Number: " + policy.getPolicyNumber() + "\n"
                + "Status: " + policy.getStatus() + "\n"
                + "Issued: " + policy.getIssuedDate() + "\n"
                + "Expires: " + policy.getExpiryDate();

        ByteArrayResource resource = new ByteArrayResource(fileContent.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=policy_" + policy.getPolicyId() + ".txt")
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }


}








