package com.hexaware.automobileinsurancesystem.exceptions;

/*public class QuoteNotFoundException extends InsuranceException {
	private static final long serialVersionUID = 1L;
    public QuoteNotFoundException(String message) {
        super("Quote not found: " + message);
    }
}
*/

public class QuoteNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuoteNotFoundException(String msg) { 
		super(msg);
	}
}