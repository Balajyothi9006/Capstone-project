package com.example.onlineBusBookingdemo.Service;

import com.example.onlineBusBookingdemo.Entity.Booking;
import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Repository.BookingRepository;
import com.example.onlineBusBookingdemo.Repository.BusRepository;
import com.example.onlineBusBookingdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRepository busRepository;

    // ✅ Create Booking

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }
    // ✅ Get all bookings of a user
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
