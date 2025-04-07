package com.example.onlineBusBookingdemo.Service;



import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<Users> getUserByname(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Users registerUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public boolean authenticateUser(String email, String rawPassword) {
        Optional<Users> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }

        return false; // user not found
    }

    public Users updateUser(Users updatedUser) {
        Users existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(updatedUser.getName());

        existingUser.setEmail(updatedUser.getEmail());


        return userRepository.save(existingUser);
    }
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
