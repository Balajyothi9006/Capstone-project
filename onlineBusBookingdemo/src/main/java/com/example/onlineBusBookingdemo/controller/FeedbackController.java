package com.example.onlineBusBookingdemo.controller;

import com.example.onlineBusBookingdemo.Entity.Feedback;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Service.FeedbackService;
import com.example.onlineBusBookingdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @GetMapping("/form/{userId}")
    public String showFeedbackForm(@PathVariable Long userId, Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("userId", userId);
        return "feedback-form";
    }

    @PostMapping("/submit")
    public String submitFeedback(@ModelAttribute Feedback feedback, @RequestParam Long userId, Model model) {
        Optional<Users> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            feedback.setUser(userOpt.get());
            feedback.setDate(LocalDate.now());
            feedbackService.saveFeedback(feedback);
            model.addAttribute("message", "Thank you for your feedback!");
            return "feedback-success";
        } else {
            model.addAttribute("error", "User not found.");
            return "error";
        }
    }

    @GetMapping("/all")
    public String viewAllFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "admin/feedback-list";
    }
}

