package com.hexaware.automobileinsurancesystem.dto;

import java.time.LocalDateTime;

import com.hexaware.automobileinsurancesystem.entities.Proposal;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProposalDTO {

    private Long proposalId;

    @NotNull
    private Long userId;

    @NotNull
    private Long vehicleId;

    @NotNull
    private Proposal.Status status;

    private LocalDateTime createdAt;
}

