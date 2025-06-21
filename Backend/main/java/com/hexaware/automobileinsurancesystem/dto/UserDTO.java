
package com.hexaware.automobileinsurancesystem.dto;

import java.time.LocalDate;

import com.hexaware.automobileinsurancesystem.entities.User;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {

    private Long userId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private LocalDate dob;

    private String address;

    @NotBlank
    private String aadhaar;

    @NotBlank
    private String pan;

    @NotNull
    private User.Role role;  

}


