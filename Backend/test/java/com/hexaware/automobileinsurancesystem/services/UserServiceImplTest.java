package com.hexaware.automobileinsurancesystem.services;

import com.hexaware.automobileinsurancesystem.entities.User;
import com.hexaware.automobileinsurancesystem.exceptions.UserNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setDob(LocalDate.of(1995, 1, 1));
        user.setAadhaar("123412341234");
        user.setPan("ABCDE1234F");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);
        User created = userService.createUser(user);
        assertEquals(user.getEmail(), created.getEmail());
    }

    @Test
    void testGetUserById() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User found = userService.getUserById(1L);
        assertEquals(user.getUserId(), found.getUserId());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetUserByEmail() throws UserNotFoundException {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        User found = userService.getUserByEmail("test@example.com");
        assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("nope@example.com")).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("nope@example.com"));
    }

    @Test
    void testGetUserByAadhaar() throws UserNotFoundException {
        when(userRepository.findByAadhaar("123412341234")).thenReturn(user);
        User found = userService.getUserByAadhaar("123412341234");
        assertEquals(user.getAadhaar(), found.getAadhaar());
    }

    @Test
    void testGetUserByAadhaar_NotFound() {
        when(userRepository.findByAadhaar("000000000000")).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.getUserByAadhaar("000000000000"));
    }

    @Test
    void testGetUserByPan() throws UserNotFoundException {
        when(userRepository.findByPan("ABCDE1234F")).thenReturn(user);
        User found = userService.getUserByPan("ABCDE1234F");
        assertEquals(user.getPan(), found.getPan());
    }

    @Test
    void testGetUserByPan_NotFound() {
        when(userRepository.findByPan("XXXXX9999X")).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.getUserByPan("XXXXX9999X"));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUsers();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateUser() throws UserNotFoundException {
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setEmail("updated@example.com");
        user.setDob(LocalDate.of(1995, 1, 1));
        updatedUser.setAadhaar("123412341234");
        updatedUser.setPan("ABCDE1234F");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void testDeleteUser() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(user);
    }
}
