
package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.automobileinsurancesystem.entities.Payment;
import com.hexaware.automobileinsurancesystem.entities.Quote;
import com.hexaware.automobileinsurancesystem.exceptions.PaymentFailedException;
import com.hexaware.automobileinsurancesystem.repositories.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private PolicyService policyService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Override
    public Payment createPayment(Payment payment) {
        logger.info("Creating payment for quote ID: {}", payment.getQuote().getQuoteId());
        Quote quote = quoteService.getQuoteById(payment.getQuote().getQuoteId());

        if (Double.compare(payment.getAmountPaid(), quote.getPremiumAmount()) != 0) {
            logger.error("Payment amount {} does not match quote premium {}", payment.getAmountPaid(), quote.getPremiumAmount());
            throw new IllegalArgumentException("Payment amount must match the quote premium");
        }

        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getStatus() == Payment.Status.Completed) {
            logger.info("Payment completed for payment ID: {}, triggering policy creation", savedPayment.getPaymentId());
            policyService.createPolicyForPayment(savedPayment);
        }

        return savedPayment;
    }

    @Override
    public Payment updatePayment(Payment payment) throws PaymentFailedException {
        getPaymentById(payment.getPaymentId());
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long paymentId) throws PaymentFailedException {
        Payment payment = getPaymentById(paymentId);
        paymentRepository.delete(payment);
    }

    @Override
    public Payment getPaymentById(Long paymentId) throws PaymentFailedException {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentFailedException("Payment not found: " + paymentId));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsByQuoteId(Long quoteId) {
        return paymentRepository.findByQuote_QuoteId(quoteId); 
    }
}
