package com.example.onlineBusBookingdemo.controllerTest;



import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Service.UserService;
import com.example.onlineBusBookingdemo.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private Users user;

    @BeforeEach
    public void setup() {
        user = new Users();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    public void testShowRegisterForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "Test User")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).registerUser(any(Users.class));
    }

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testLoginSuccessAsUser() throws Exception {
        when(userService.authenticateUser("test@example.com", "password123")).thenReturn(true);
        when(userService.getUserByname("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-buses"))
                .andExpect(model().attributeExists("userId"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        when(userService.authenticateUser("test@example.com", "wrongpass")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "test@example.com")
                        .param("password", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void testUserProfile() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testChangePasswordSuccess() throws Exception {
        doNothing().when(userService).changePassword(1L, "oldpass", "newpass");

        mockMvc.perform(MockMvcRequestBuilders.post("/change-password")
                        .param("userId", "1")
                        .param("oldPassword", "oldpass")
                        .param("newPassword", "newpass")
                        .param("confirmPassword", "newpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    public void testChangePasswordMismatch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/change-password")
                        .param("userId", "1")
                        .param("oldPassword", "oldpass")
                        .param("newPassword", "newpass")
                        .param("confirmPassword", "differentpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("error"));
    }
}

