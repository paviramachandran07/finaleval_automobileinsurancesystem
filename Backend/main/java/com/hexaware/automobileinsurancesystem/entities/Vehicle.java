package com.hexaware.automobileinsurancesystem.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @NotBlank(message = "Vehicle type is required")
    @Size(min = 2, max = 50, message = "Vehicle type must be 2-50 characters")
    private String type;

    @NotBlank(message = "Model is required")
    @Size(min = 2, max = 50, message = "Model must be 2-50 characters")
    @Pattern(regexp = "[A-Za-z0-9\\s-]{2,50}", message = "Model must be 2-50 characters (letters, numbers, spaces, hyphens)")
    private String model;

    @NotBlank(message = "Registration number is required")
    @Column(unique = true)
    @Pattern(regexp = "^[A-Z]{2}\\s\\d{2}\\s[A-Z]{2}\\s\\d{4}$", message = "Registration number must be in format XX DD XX DDDD (e.g., MH 12 AB 1234)")
    private String registrationNumber;

    @NotNull(message = "Year of manufacture is required")
    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2025, message = "Year cannot be in the future")
    private Integer yearOfManufacture;
}

