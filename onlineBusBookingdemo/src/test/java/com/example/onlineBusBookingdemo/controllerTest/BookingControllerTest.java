package com.example.onlineBusBookingdemo.controllerTest;

import com.example.onlineBusBookingdemo.Entity.Booking;
import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Service.BookingService;
import com.example.onlineBusBookingdemo.Service.BusService;
import com.example.onlineBusBookingdemo.Service.UserService;

import com.example.onlineBusBookingdemo.controller.BookingController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.ui.Model;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private BusService busService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private BookingController bookingController;

    public BookingControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowBookingPage() {
        String view = bookingController.showBookingPage(1L, 2L, model);

        verify(model).addAttribute("userId", 2L);
        verify(model).addAttribute("busId", 1L);
        assertEquals("booking", view);
    }

    @Test
    void testConfirmBookingSuccess() {
        Bus mockBus = new Bus();
        mockBus.setId(1L);
        mockBus.setPricePerSeat(300);
        mockBus.setTravelDate(LocalDate.of(2024, 12, 20));

        Users mockUser = new Users();
        mockUser.setId(2L);

        Booking booking = new Booking();
        booking.setSeatCount(2);

        when(busService.getBusById(1L)).thenReturn(mockBus);
        when(userService.getUserById(2L)).thenReturn(Optional.of(mockUser));

        String result = bookingController.confirmBooking(booking, 1L, 2L, 2, model);

        verify(bookingService).saveBooking(any(Booking.class));
        verify(model).addAttribute("message", "Booking confirmed!");
        verify(model).addAttribute(eq("booking"), any(Booking.class));
        assertEquals("booking-success", result);
    }

    @Test
    void testConfirmBookingInvalidUserOrBus() {
        when(busService.getBusById(1L)).thenReturn(null);
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        Booking booking = new Booking();
        String view = bookingController.confirmBooking(booking, 1L, 2L, 1, model);

        verify(model).addAttribute("error", "Invalid booking data.");
        assertEquals("error", view);
    }

    @Test
    void testUserBookings() {
        List<Booking> mockBookings = List.of(new Booking(), new Booking());
        when(bookingService.getBookingsByUser(5L)).thenReturn(mockBookings);

        String view = bookingController.userBookings(5L, model);

        verify(model).addAttribute("bookings", mockBookings);
        assertEquals("booking/history", view);
    }
}
