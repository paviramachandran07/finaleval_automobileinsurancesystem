package com.hexaware.automobileinsurancesystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VehicleDTO {

    private Long vehicleId;

    @NotNull
    private Long userId;

    @NotBlank
    private String type;

    @NotBlank
    private String model;

    @NotBlank
    private String registrationNumber;

    private Integer yearOfManufacture;
}


