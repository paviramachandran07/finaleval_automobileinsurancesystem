package com.hexaware.automobileinsurancesystem.exceptions;

/*import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle all "Not Found" exceptions with 404 status
    @ExceptionHandler({
        UserNotFoundException.class,
        OfficerNotFoundException.class,
        VehicleNotFoundException.class,
        ProposalNotFoundException.class,
        QuoteNotFoundException.class,
        PolicyNotFoundException.class
    })
    public ResponseEntity<String> handleNotFound(InsuranceException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handle payment failures with 400 status
    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<String> handlePaymentFailed(PaymentFailedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Fallback for all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllOther(Exception ex) {
        return ResponseEntity.internalServerError().body("Something went wrong");
    }
}
*/




import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

 @ExceptionHandler(UserNotFoundException.class)
 public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex, WebRequest req) {
     return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
 }

 @ExceptionHandler({ VehicleNotFoundException.class, ProposalNotFoundException.class,
                     QuoteNotFoundException.class, PaymentFailedException.class,
                     PolicyNotFoundException.class })
 public ResponseEntity<String> handleEntityNotFound(RuntimeException ex, WebRequest req) {
     return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
 }

 @ExceptionHandler(IllegalArgumentException.class)
 public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
     return ResponseEntity.badRequest().body(ex.getMessage());
 }

 
 @ExceptionHandler(Exception.class)
 public ResponseEntity<String> handleGeneralError(Exception ex, WebRequest req) {
     return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
 }
}