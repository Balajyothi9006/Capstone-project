package com.example.onlineBusBookingdemo.controller;

import com.example.onlineBusBookingdemo.Entity.Booking;
import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Service.BookingService;
import com.example.onlineBusBookingdemo.Service.BusService;
import com.example.onlineBusBookingdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller

public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BusService busService;
    @Autowired
    private UserService userService;
    @GetMapping("/bus/{busId}/{userId}/select")
    public String showBookingPage(@PathVariable("busId") Long busId,
                                  @PathVariable("userId") Long userId,Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("busId", busId);

        return "booking"; // booking.html
    }

    @PostMapping("/confirm-booking")
    public String confirmBooking(@ModelAttribute Booking booking,
                                 @RequestParam("busId") Long busId,
                                 @RequestParam("userId") Long userId,@RequestParam("seatCount") int seatCount,
                                 Model model) {

        Bus bus = busService.getBusById(busId);
        Optional<Users> user = userService.getUserById(userId);

        if (bus == null || user == null) {
            model.addAttribute("error", "Invalid booking data.");
            return "error";
        }
        int totalAmount = seatCount*bus.getPricePerSeat();

        booking.setBus(bus);
        booking.setTotalAmount(totalAmount);
        booking.setBookingDate(LocalDate.now());
        booking.setTravelDate(bus.getTravelDate());
        bookingService.saveBooking(booking);

        model.addAttribute("message", "Booking confirmed!");
        model.addAttribute("booking", booking);
        model.addAttribute("userId");
        return "booking-success";
    }

    @GetMapping("/user/{userId}")
    public String userBookings(@PathVariable Long userId, Model model) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        model.addAttribute("bookings", bookings);
        return "booking/history";
    }
}