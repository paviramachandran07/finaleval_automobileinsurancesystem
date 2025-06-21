package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.exceptions.UserNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User createUser(User user) {
        logger.info("Creating user: {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundException("Email not found: " + email);
        return user;
    }

    @Override
    public User getUserByAadhaar(String aadhaar) throws UserNotFoundException {
        User user = userRepository.findByAadhaar(aadhaar);
        if (user == null) throw new UserNotFoundException("Aadhaar not found: " + aadhaar);
        return user;
    }

    @Override
    public User getUserByPan(String pan) throws UserNotFoundException {
        User user = userRepository.findByPan(pan);
        if (user == null) throw new UserNotFoundException("PAN not found: " + pan);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) throws UserNotFoundException {
        User existing = getUserById(id);
        user.setUserId(existing.getUserId());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
