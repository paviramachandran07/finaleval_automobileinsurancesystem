package com.hexaware.automobileinsurancesystem.dto;

import java.time.LocalDateTime;

import com.hexaware.automobileinsurancesystem.entities.Policy;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PolicyDTO {

    private Long policyId;

    @NotNull
    private Long paymentId;

    @NotBlank
    private String policyNumber;

    private LocalDateTime issuedDate;

    private LocalDateTime expiryDate;

    @NotNull
    private Policy.Status status;


    private String documentPath;
    
    private String terms;
}

