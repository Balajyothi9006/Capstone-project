package com.example.onlineBusBookingdemo.Repository;

import com.example.onlineBusBookingdemo.Entity.Booking;
import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRepository busRepository;

    @Test
    void testFindByUserId() {
        // Create test user
        Users user = new Users();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("pass");
        Users savedUser = userRepository.save(user);

        // Create test bus
        Bus bus = new Bus();
        bus.setSource("City A");
        bus.setDestination("City B");
        bus.setDepartureTime(LocalTime.of(10, 0)); // 10:00 AM
        bus.setArrivalTime(LocalTime.of(14, 0));
        bus.setTotalSeats(40);
        bus.setPricePerSeat(500);
        bus.setTravelDate(LocalDate.of(2024, 12, 15));
        Bus savedBus = busRepository.save(bus);

        // Create a booking for this user and bus
        Booking booking = new Booking();
        booking.setUser(savedUser);
        booking.setBus(savedBus);
        booking.setSeatCount(2);
        booking.setBookingDate(LocalDate.now());
        booking.setTravelDate(LocalDate.of(2024, 12, 15));
        booking.setTotalAmount(1000);
        booking.setPaymentOption("Card");
        booking.setSeatPreferences("A1,A2");

        bookingRepository.save(booking);

        // Test the custom method
        List<Booking> foundBookings = bookingRepository.findByUserId(savedUser.getId());

        assertThat(foundBookings).isNotEmpty();
        assertThat(foundBookings.get(0).getUser().getEmail()).isEqualTo("john@example.com");
    }
}

