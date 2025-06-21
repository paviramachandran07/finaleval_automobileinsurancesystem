package com.hexaware.automobileinsurancesystem.exceptions;

/*public class PolicyNotFoundException extends InsuranceException {
	private static final long serialVersionUID = 1L;
    public PolicyNotFoundException(String message) {
        super("Policy not found: " + message);
    }
}
*/

public class PolicyNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PolicyNotFoundException(String msg) { 
		super(msg); 
	}
}