package com.example.onlineBusBookingdemo.serviceTest;

import com.example.onlineBusBookingdemo.Entity.Feedback;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Repository.FeedbackRepository;
import com.example.onlineBusBookingdemo.Service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback feedback1;
    private Feedback feedback2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Users user = new Users();
        user.setId(1L);
        user.setName("Test User");

        feedback1 = new Feedback();
        feedback1.setId(1L);
        feedback1.setMessage("Great service");
        feedback1.setDate(LocalDate.now());
        feedback1.setUser(user);

        feedback2 = new Feedback();
        feedback2.setId(2L);
        feedback2.setMessage("Very comfortable journey");
        feedback2.setDate(LocalDate.now());
        feedback2.setUser(user);
    }

    @Test
    void testSaveFeedback() {
        when(feedbackRepository.save(feedback1)).thenReturn(feedback1);

        feedbackService.saveFeedback(feedback1);

        verify(feedbackRepository, times(1)).save(feedback1);
    }

    @Test
    void testGetFeedbackByUserId() {
        when(feedbackRepository.findByUserId(1L)).thenReturn(Arrays.asList(feedback1, feedback2));

        List<Feedback> feedbackList = feedbackService.getFeedbackByUserId(1L);

        assertEquals(2, feedbackList.size());
        verify(feedbackRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetAllFeedbacks() {
        when(feedbackRepository.findAll()).thenReturn(Arrays.asList(feedback1, feedback2));

        List<Feedback> feedbackList = feedbackService.getAllFeedbacks();

        assertEquals(2, feedbackList.size());
        verify(feedbackRepository, times(1)).findAll();
    }
}
