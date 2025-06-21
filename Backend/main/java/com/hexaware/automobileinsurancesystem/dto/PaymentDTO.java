package com.hexaware.automobileinsurancesystem.dto;

import java.time.LocalDateTime;

import com.hexaware.automobileinsurancesystem.entities.Payment;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PaymentDTO {

    private Long paymentId;

    @NotNull
    private Long quoteId;

    private LocalDateTime paymentDate;

    @NotNull
    private Double amountPaid;

    @NotNull
    private Payment.Status status;

}



