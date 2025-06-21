package com.hexaware.automobileinsurancesystem.dto;

import com.hexaware.automobileinsurancesystem.entities.Quote;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class QuoteDTO {

    private Long quoteId;

    @NotNull
    private Long proposalId;

    @NotNull
    private Double premiumAmount;

    @NotNull
    private Quote.Status status;
    
    private String coverageDetails;

}
