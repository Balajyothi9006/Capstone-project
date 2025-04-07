package com.example.onlineBusBookingdemo.controllerTest;


import com.example.onlineBusBookingdemo.Entity.Feedback;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Service.FeedbackService;
import com.example.onlineBusBookingdemo.controller.FeedbackController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDate;
import java.util.Arrays;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(FeedbackController.class)
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    private Feedback feedback1;
    private Feedback feedback2;

    @BeforeEach
    void setup() {
        Users user = new Users();
        user.setId(1L);
        user.setName("Test User");

        feedback1 = new Feedback();
        feedback1.setId(1L);
        feedback1.setMessage("Great trip!");
        feedback1.setDate(LocalDate.now());
        feedback1.setUser(user);

        feedback2 = new Feedback();
        feedback2.setId(2L);
        feedback2.setMessage("Very smooth ride");
        feedback2.setDate(LocalDate.now());
        feedback2.setUser(user);
    }

    @Test
    void testShowFeedbackForm() throws Exception {
        mockMvc.perform(get("/feedback/form").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback-form"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attributeExists("feedback"));
    }

    @Test
    void testSubmitFeedback() throws Exception {
        mockMvc.perform(post("/feedback/submit")
                        .param("message", "Awesome!")
                        .param("user.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/feedback/user/1"));

        verify(feedbackService, times(1)).saveFeedback(any(Feedback.class));
    }

    @Test
    void testViewUserFeedback() throws Exception {
        when(feedbackService.getFeedbackByUserId(1L)).thenReturn(Arrays.asList(feedback1, feedback2));

        mockMvc.perform(get("/feedback/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-feedback"))
                .andExpect(model().attributeExists("feedbackList"))
                .andExpect(model().attribute("feedbackList", hasSize(2)));
    }

    @Test
    void testViewAllFeedbacks() throws Exception {
        when(feedbackService.getAllFeedbacks()).thenReturn(Arrays.asList(feedback1, feedback2));

        mockMvc.perform(get("/feedback/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-feedback"))
                .andExpect(model().attributeExists("feedbackList"))
                .andExpect(model().attribute("feedbackList", hasSize(2)));
    }
}
