package com.hexaware.automobileinsurancesystem.exceptions;

/*public class PaymentFailedException extends InsuranceException {
	private static final long serialVersionUID = 1L;
    public PaymentFailedException(String message) {
        super("Payment failed: " + message);
    }
}
 */

public class PaymentFailedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaymentFailedException(String msg) { 
		super(msg); 
	}
}