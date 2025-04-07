package com.example.onlineBusBookingdemo.controller;


import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller

public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new Users()); // Using Users entity
        return "register"; // Loads templates/login.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Users user) {
        userService.registerUser(user);
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new Users()); // Using Users entity
        return "login"; // Loads templates/login.html
    }

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "profile";
    }
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        boolean isAuthenticated = userService.authenticateUser(username,password);

        if (!isAuthenticated) {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        }

        Optional<Users> userOptional = userService.getUserByname(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Long userId = user.getId();
            model.addAttribute("userId", userId);
            return "search-buses";
        } else {
            model.addAttribute("errorMessage", "User not found.");
            return "login";
        }

    }


    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("user") Users user) {
        userService.updateUser(user); //
        return "redirect:/profile";


    }




    @PostMapping("/change-password")
    public String changePassword(@RequestParam Long userId,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirm password do not match.");
            return "profile"; // return back to form
        }

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            model.addAttribute("success", "Password updated successfully!");
            return "profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "profile";
        }
    }
}
