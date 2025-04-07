package com.example.onlineBusBookingdemo.serviceTest;


import com.example.onlineBusBookingdemo.Entity.Booking;
import com.example.onlineBusBookingdemo.Repository.BookingRepository;
import com.example.onlineBusBookingdemo.Repository.UserRepository;
import com.example.onlineBusBookingdemo.Repository.BusRepository;

import com.example.onlineBusBookingdemo.Service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBooking() {
        Booking booking = new Booking();
        booking.setSeatCount(2);
        booking.setBookingDate(LocalDate.now());
        booking.setTotalAmount(1000);

        bookingService.saveBooking(booking);

        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testGetBookingsByUser() {
        Long userId = 1L;
        List<Booking> mockBookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setSeatCount(2);
        Booking booking2 = new Booking();
        booking2.setSeatCount(3);
        mockBookings.add(booking1);
        mockBookings.add(booking2);

        when(bookingRepository.findByUserId(userId)).thenReturn(mockBookings);

        List<Booking> bookings = bookingService.getBookingsByUser(userId);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getSeatCount()).isEqualTo(2);
        assertThat(bookings.get(1).getSeatCount()).isEqualTo(3);
    }
}

