package com.example.onlineBusBookingdemo.serviceTest;



import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Repository.UserRepository;
import com.example.onlineBusBookingdemo.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new Users();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
    }

    @Test
    void testGetAllUsers() {
        List<Users> userList = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(userList);

        List<Users> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByName() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<Users> user = userService.getUserByname("test@example.com");

        assertTrue(user.isPresent());
        assertEquals("test@example.com", user.get().getEmail());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<Users> user = userService.getUserById(1L);

        assertTrue(user.isPresent());
        assertEquals(1L, user.get().getId());
    }

    @Test
    void testRegisterUser() {
        Users newUser = new Users();
        newUser.setEmail("new@example.com");
        newUser.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(newUser);

        Users registered = userService.registerUser(newUser);

        assertEquals("encodedPassword", newUser.getPassword());
        verify(userRepository).save(newUser);
    }

    @Test
    void testAuthenticateUser_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        boolean result = userService.authenticateUser("test@example.com", "rawPassword");

        assertTrue(result);
    }

    @Test
    void testAuthenticateUser_Failure() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        boolean result = userService.authenticateUser("test@example.com", "wrongPassword");

        assertFalse(result);
    }

    @Test
    void testUpdateUser_Success() {
        Users updatedUser = new Users();
        updatedUser.setId(1L);
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(Users.class))).thenReturn(updatedUser);

        Users result = userService.updateUser(updatedUser);

        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void testChangePassword_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncoded");
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.changePassword(1L, "oldPassword", "newPassword"));

        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void testChangePassword_WrongOldPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongOld", "encodedPassword")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.changePassword(1L, "wrongOld", "newPass"));

        assertEquals("Old password is incorrect", ex.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        boolean result = userService.deleteUser(1L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }
}

