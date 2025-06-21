package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import com.hexaware.automobileinsurancesystem.entities.Payment;
import com.hexaware.automobileinsurancesystem.entities.Policy;
import com.hexaware.automobileinsurancesystem.exceptions.PolicyNotFoundException;

public interface PolicyService {
    Policy createPolicy(Policy policy);
    Policy createPolicyForPayment(Payment payment);
    Policy updatePolicy(Policy policy) throws PolicyNotFoundException;
    void deletePolicy(Long policyId) throws PolicyNotFoundException;
    Policy getPolicyById(Long policyId) throws PolicyNotFoundException;
    Policy getPolicyByIdSecure(Long policyId, String userEmail);
    Policy getPolicyByNumber(String policyNumber) throws PolicyNotFoundException;

    List<Policy> getAllPolicies();
    List<Policy> getPoliciesByPaymentId(Long paymentId);
    Policy getPolicyByQuoteId(Long quoteId);
    List<Policy> getPoliciesByUserId(Long userId);
}




