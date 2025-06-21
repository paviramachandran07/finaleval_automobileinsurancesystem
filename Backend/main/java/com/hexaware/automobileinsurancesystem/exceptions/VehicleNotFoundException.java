package com.hexaware.automobileinsurancesystem.exceptions;

/*public class VehicleNotFoundException extends InsuranceException {
	private static final long serialVersionUID = 1L;
    public VehicleNotFoundException(String message) {
        super("Vehicle not found: " + message);
    }
}
*/

public class VehicleNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VehicleNotFoundException(String msg) {
		super(msg); 
		}
}