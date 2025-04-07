package com.example.onlineBusBookingdemo.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Optional;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
    private LocalDate bookingDate;
    private LocalDate travelDate;
    private int seatCount;
    private int totalAmount;
    private String paymentOption;
    private String seatPreferences;

    public Booking() {
    }

    public Booking(Long id, Users user, Bus bus, LocalDate bookingDate,LocalDate travelDate, int seatCount, int totalAmount, String paymentOption, String seatPreferences) {
        this.id = id;
        this.user = user;
        this.bus = bus;
        this.bookingDate = bookingDate;
        this.seatCount = seatCount;
        this.totalAmount = totalAmount;
        this.paymentOption = paymentOption;
        this.seatPreferences = seatPreferences;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getSeatPreferences() {
        return seatPreferences;
    }

    public void setSeatPreferences(String seatPreferences) {
        this.seatPreferences = seatPreferences;
    }
}