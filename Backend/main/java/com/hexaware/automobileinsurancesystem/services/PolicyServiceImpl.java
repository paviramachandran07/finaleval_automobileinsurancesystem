package com.hexaware.automobileinsurancesystem.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.automobileinsurancesystem.entities.Payment;
import com.hexaware.automobileinsurancesystem.entities.Policy;
import com.hexaware.automobileinsurancesystem.exceptions.PolicyNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.PolicyRepository;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    private static final Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);

    @Override
    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    @Override
    public Policy createPolicyForPayment(Payment payment) {
        logger.info("Creating policy for payment ID: {}", payment.getPaymentId());
        Policy policy = new Policy();
        policy.setPayment(payment);
        policy.setPolicyNumber("POL-2025-" + String.format("%04d", payment.getPaymentId()));
        policy.setIssuedDate(LocalDateTime.now());
        policy.setExpiryDate(LocalDateTime.now().plusYears(1));
        policy.setTerms("Standard policy terms for vehicle " +
                payment.getQuote().getProposal().getVehicle().getRegistrationNumber());
        policy.setStatus(Policy.Status.Active);
        policy.setDocumentPath("/documents/" + policy.getPolicyNumber() + ".pdf");
        Policy savedPolicy = policyRepository.save(policy);
        logger.info("Policy created with ID: {} for payment ID: {}", savedPolicy.getPolicyId(), payment.getPaymentId());
        return savedPolicy;
    }

    @Override
    public Policy updatePolicy(Policy policy) throws PolicyNotFoundException {
        getPolicyById(policy.getPolicyId());
        return policyRepository.save(policy);
    }

    @Override
    public void deletePolicy(Long policyId) throws PolicyNotFoundException {
        Policy policy = getPolicyById(policyId);
        policyRepository.delete(policy);
    }

    @Override
    public Policy getPolicyById(Long policyId) throws PolicyNotFoundException {
        return policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyId));
    }

    @Override
    public Policy getPolicyByIdSecure(Long policyId, String userEmail) throws PolicyNotFoundException {
        Policy policy = getPolicyById(policyId);
        String emailOnPolicy = policy.getPayment()
                .getQuote()
                .getProposal()
                .getUser()
                .getEmail();

        if (!emailOnPolicy.equals(userEmail)) {
            throw new PolicyNotFoundException("Unauthorized access to policy ID: " + policyId);
        }

        return policy;
    }

    @Override
    public Policy getPolicyByNumber(String policyNumber) throws PolicyNotFoundException {
        logger.info("Fetching policy with policy number: {}", policyNumber);
        return policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> {
                    logger.error("Policy not found with policy number: {}", policyNumber);
                    return new PolicyNotFoundException("Policy not found with policy number: " + policyNumber);
                });
    }

    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public List<Policy> getPoliciesByPaymentId(Long paymentId) {
        return policyRepository.findByPayment_PaymentId(paymentId);
    }

    
    @Override
    public List<Policy> getPoliciesByUserId(Long userId) {
        return policyRepository.findAll().stream()
                .filter(p -> p.getPayment()
                              .getQuote()
                              .getProposal()
                              .getUser()
                              .getUserId()
                              .equals(userId))
                .collect(Collectors.toList());
    }


    @Override
    public Policy getPolicyByQuoteId(Long quoteId) {
        return policyRepository.findAll()
                .stream()
                .filter(p -> p.getPayment().getQuote().getQuoteId().equals(quoteId))
                .findFirst()
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found for quote: " + quoteId));
    }

}




