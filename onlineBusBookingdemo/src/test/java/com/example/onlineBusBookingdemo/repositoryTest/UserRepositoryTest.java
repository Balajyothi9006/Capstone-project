package com.example.onlineBusBookingdemo.repositoryTest;



import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and retrieve user by email")
    public void testFindByEmail() {
        // Given
        Users user = new Users();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("securepass");

        userRepository.save(user);

        // When
        Optional<Users> foundUser = userRepository.findByEmail("john@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty for non-existing email")
    public void testFindByEmailNotFound() {
        Optional<Users> user = userRepository.findByEmail("doesnotexist@example.com");
        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("Should save and find user by ID")
    public void testFindById() {
        Users user = new Users();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("alicepass");

        Users saved = userRepository.save(user);
        Optional<Users> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    @DisplayName("Should delete user by ID")
    public void testDeleteById() {
        Users user = new Users();
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setPassword("bobpass");

        Users saved = userRepository.save(user);
        Long id = saved.getId();

        userRepository.deleteById(id);
        Optional<Users> deleted = userRepository.findById(id);

        assertFalse(deleted.isPresent());
    }
}

