package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import com.hexaware.automobileinsurancesystem.entities.Payment;
import com.hexaware.automobileinsurancesystem.exceptions.PaymentFailedException;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment) throws PaymentFailedException;
    void deletePayment(Long paymentId) throws PaymentFailedException;
    Payment getPaymentById(Long paymentId) throws PaymentFailedException;
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByQuoteId(Long quoteId);
}
