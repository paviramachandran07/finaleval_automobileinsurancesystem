package com.hexaware.automobileinsurancesystem.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.hexaware.automobileinsurancesystem.entities.Payment;
import com.hexaware.automobileinsurancesystem.entities.Quote;
import com.hexaware.automobileinsurancesystem.exceptions.PaymentFailedException;
import com.hexaware.automobileinsurancesystem.repositories.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Quote quote = new Quote();
        quote.setQuoteId(1L);
        payment = new Payment();
        payment.setPaymentId(1L);
        payment.setQuote(quote);
    }

    @Test
    void testCreatePayment() {
        when(paymentRepository.save(payment)).thenReturn(payment);
        Payment result = paymentService.createPayment(payment);
        assertEquals(payment, result);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testUpdatePaymentSuccess() throws PaymentFailedException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        Payment result = paymentService.updatePayment(payment);
        assertEquals(payment, result);
    }

    @Test
    void testUpdatePaymentNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PaymentFailedException.class, () -> paymentService.updatePayment(payment));
    }

    @Test
    void testDeletePaymentByIdSuccess() throws PaymentFailedException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        paymentService.deletePayment(1L);
        verify(paymentRepository, times(1)).delete(payment);
    }

    @Test
    void testDeletePaymentByIdNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PaymentFailedException.class, () -> paymentService.deletePayment(1L));
    }

    @Test
    void testGetPaymentByIdSuccess() throws PaymentFailedException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        Payment result = paymentService.getPaymentById(1L);
        assertEquals(payment, result);
    }

    @Test
    void testGetPaymentByIdNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PaymentFailedException.class, () -> paymentService.getPaymentById(1L));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentRepository.findAll()).thenReturn(payments);
        assertEquals(1, paymentService.getAllPayments().size());
    }

    @Test
    void testGetPaymentsByQuoteId() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentRepository.findByQuote_QuoteId(1L)).thenReturn(payments);
        List<Payment> result = paymentService.getPaymentsByQuoteId(1L);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }
}

