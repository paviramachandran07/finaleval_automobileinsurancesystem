package com.hexaware.automobileinsurancesystem.exceptions;
/*public class ProposalNotFoundException extends InsuranceException {
	private static final long serialVersionUID = 1L;
    public ProposalNotFoundException(String message) {
        super("Proposal not found: " + message);
    }
}*/

public class ProposalNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProposalNotFoundException(String msg) { super(msg); }
}
