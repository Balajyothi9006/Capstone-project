package com.example.onlineBusBookingdemo.repositoryTest;



import com.example.onlineBusBookingdemo.Entity.Booking;
import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Entity.Users;
import com.example.onlineBusBookingdemo.Repository.BookingRepository;
import com.example.onlineBusBookingdemo.Repository.BusRepository;
import com.example.onlineBusBookingdemo.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRepository busRepository;

    private Users testUser;
    private Bus testBus;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        testBus = new Bus();
        testBus.setName("Express Line");
        testBus.setSource("City A");
        testBus.setDestination("City B");
        testBus.setTravelDate(LocalDate.now().plusDays(5));
        testBus.setDepartureTime(java.time.LocalTime.of(9, 0));
        testBus.setArrivalTime(java.time.LocalTime.of(13, 0));
        testBus.setTotalSeats(40);
        testBus.setPricePerSeat(300);
        testBus = busRepository.save(testBus);
    }

    @Test
    void testFindByUserId() {
        Booking booking = new Booking();
        booking.setUser(testUser);
        booking.setBus(testBus);
        booking.setBookingDate(LocalDate.now());
        booking.setTravelDate(testBus.getTravelDate());
        booking.setSeatCount(2);
        booking.setTotalAmount(600);
        booking.setPaymentOption("Card");
        booking.setSeatPreferences("Window");

        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByUserId(testUser.getId());

        assertThat(bookings).isNotEmpty();
        assertThat(bookings.get(0).getUser().getEmail()).isEqualTo("john@example.com");
        assertThat(bookings.get(0).getBus().getName()).isEqualTo("Express Line");
    }
}
