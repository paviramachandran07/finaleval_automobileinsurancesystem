package com.hexaware.automobileinsurancesystem.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quoteId;

    @ManyToOne
    @JoinColumn(name = "proposal_id", nullable = false)
    private Proposal proposal;

 
    @NotNull
    private Double premiumAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

     
    private String coverageDetails;
    
    public enum Status {
        Pending,
        Sent,
        Accepted
    }

}
