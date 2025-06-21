package com.hexaware.automobileinsurancesystem.restcontrollers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.automobileinsurancesystem.dto.UserDTO;
import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getPassword(),
                user.getDob(), user.getAddress(), user.getAadhaar(), user.getPan(), user.getRole());
    }

    private User convertToEntity(UserDTO dto) {
        return new User(dto.getUserId(), dto.getName(), dto.getEmail(), dto.getPassword(), dto.getDob(),
                dto.getAddress(), dto.getAadhaar(), dto.getPan(), dto.getRole(), null, null);
    }

 
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO dto) {
        logger.info("Creating user with email: {}", dto.getEmail());
        User created = userService.createUser(convertToEntity(dto));
        logger.info("User created with ID: {}", created.getUserId());
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    
    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        return ResponseEntity.ok(convertToDTO(userService.getUserById(id)));
    }

    
    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userService.getAllUsers().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    
    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO dto) {
        logger.info("Updating user with ID: {}", id);
        User updated = userService.updateUser(id, convertToEntity(dto));
        logger.info("User updated with ID: {}", id);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    
    @PreAuthorize("hasAuthority('ROLE_OFFICER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.warn("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    

}

