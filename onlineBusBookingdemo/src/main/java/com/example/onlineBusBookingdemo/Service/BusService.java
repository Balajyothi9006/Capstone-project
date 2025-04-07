package com.example.onlineBusBookingdemo.Service;

import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
    public List<Bus> searchBuses(String source, String destination) {
        return busRepository.findBySourceAndDestination(source, destination);
    }
    public Bus addBus(Bus bus) {
        return busRepository.save(bus);
    }
    public Bus getBusById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found"));
    }

    public void deleteBus(Long busId) {
        if (!busRepository.existsById(busId)) {
            throw new RuntimeException("Bus with ID " + busId + " not found.");
        }
        busRepository.deleteById(busId);
    }

}