package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.exceptions.UserNotFoundException;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id) throws UserNotFoundException;
    User getUserByEmail(String email) throws UserNotFoundException;
    User getUserByAadhaar(String aadhaar) throws UserNotFoundException;
    User getUserByPan(String pan) throws UserNotFoundException;
    List<User> getAllUsers();
    User updateUser(Long id, User user) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
}
