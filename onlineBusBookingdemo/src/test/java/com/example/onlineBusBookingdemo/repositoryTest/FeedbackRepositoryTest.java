package com.example.onlineBusBookingdemo.repositoryTest;

import com.example.onlineBusBookingdemo.Entity.Feedback;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Repository.FeedbackRepository;
import com.example.onlineBusBookingdemo.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindByUserId() {
        Users user = new Users();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("pass123");
        Users savedUser = userRepository.save(user);

        Feedback feedback = new Feedback();
        feedback.setMessage("Awesome trip!");
        feedback.setDate(LocalDate.now());
        feedback.setUser(savedUser);

        feedbackRepository.save(feedback);

        List<Feedback> feedbacks = feedbackRepository.findByUserId(savedUser.getId());
        assertEquals(1, feedbacks.size());
        assertEquals("Awesome trip!", feedbacks.get(0).getMessage());
    }
}

