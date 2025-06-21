package com.hexaware.automobileinsurancesystem.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.hexaware.automobileinsurancesystem.entities.Policy;
import com.hexaware.automobileinsurancesystem.exceptions.PolicyNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.PolicyRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PolicyServiceImplTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PolicyServiceImpl policyService;

    private Policy policy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        policy = new Policy();
        policy.setPolicyId(1L);
        policy.setPolicyNumber("PN123");
    }

    @Test
    void testCreatePolicy() {
        when(policyRepository.save(policy)).thenReturn(policy);
        Policy result = policyService.createPolicy(policy);
        assertEquals(policy, result);
        verify(policyRepository, times(1)).save(policy);
    }

    @Test
    void testUpdatePolicySuccess() throws PolicyNotFoundException {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(policyRepository.save(policy)).thenReturn(policy);
        Policy result = policyService.updatePolicy(policy);
        assertEquals(policy, result);
    }

    @Test
    void testUpdatePolicyNotFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PolicyNotFoundException.class, () -> policyService.updatePolicy(policy));
    }

    @Test
    void testDeletePolicyByIdSuccess() throws PolicyNotFoundException {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        policyService.deletePolicy(1L);
        verify(policyRepository, times(1)).delete(policy);
    }

    @Test
    void testDeletePolicyByIdNotFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PolicyNotFoundException.class, () -> policyService.deletePolicy(1L));
    }

    @Test
    void testGetPolicyByIdSuccess() throws PolicyNotFoundException {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        Policy result = policyService.getPolicyById(1L);
        assertEquals(policy, result);
    }

    @Test
    void testGetPolicyByIdNotFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PolicyNotFoundException.class, () -> policyService.getPolicyById(1L));
    }

    @Test
    void testGetAllPolicies() {
        List<Policy> policies = Arrays.asList(policy);
        when(policyRepository.findAll()).thenReturn(policies);
        assertEquals(1, policyService.getAllPolicies().size());
    }

    @Test
    void testGetPolicyByNumberSuccess() throws PolicyNotFoundException {
        when(policyRepository.findByPolicyNumber("PN123")).thenReturn(Optional.of(policy));
        Policy result = policyService.getPolicyByNumber("PN123");
        assertEquals(policy, result);
    }

    @Test
    void testGetPolicyByNumberNotFound() {
        when(policyRepository.findByPolicyNumber("PN123")).thenReturn(Optional.empty());
        assertThrows(PolicyNotFoundException.class, () -> policyService.getPolicyByNumber("PN123"));
    }
}

