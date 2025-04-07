package com.example.onlineBusBookingdemo.serviceTest;




import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Repository.BusRepository;
import com.example.onlineBusBookingdemo.Service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BusServiceTest {

    @InjectMocks
    private BusService busService;

    @Mock
    private BusRepository busRepository;

    private Bus bus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bus = new Bus();
        bus.setId(1L);
        bus.setName("Test Bus");
        bus.setSource("City A");
        bus.setDestination("City B");
        bus.setTravelDate(LocalDate.of(2025, 4, 10));
        bus.setDepartureTime(LocalTime.of(10, 0));
        bus.setArrivalTime(LocalTime.of(14, 0));
        bus.setTotalSeats(40);
        bus.setPricePerSeat(300);
    }

    @Test
    void testGetAllBuses() {
        List<Bus> busList = List.of(bus);
        when(busRepository.findAll()).thenReturn(busList);

        List<Bus> result = busService.getAllBuses();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(busRepository, times(1)).findAll();
    }

    @Test
    void testSearchBuses() {
        List<Bus> busList = List.of(bus);
        when(busRepository.findBySourceAndDestination("City A", "City B")).thenReturn(busList);

        List<Bus> result = busService.searchBuses("City A", "City B");

        assertThat(result).contains(bus);
        verify(busRepository, times(1)).findBySourceAndDestination("City A", "City B");
    }

    @Test
    void testAddBus() {
        when(busRepository.save(any(Bus.class))).thenReturn(bus);

        Bus savedBus = busService.addBus(bus);

        assertThat(savedBus.getName()).isEqualTo("Test Bus");
        verify(busRepository, times(1)).save(bus);
    }

    @Test
    void testGetBusById_Found() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));

        Bus foundBus = busService.getBusById(1L);

        assertThat(foundBus).isNotNull();
        assertThat(foundBus.getId()).isEqualTo(1L);
    }

    @Test
    void testGetBusById_NotFound() {
        when(busRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> busService.getBusById(2L));
    }

    @Test
    void testDeleteBus_Success() {
        when(busRepository.existsById(1L)).thenReturn(true);

        busService.deleteBus(1L);

        verify(busRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBus_NotFound() {
        when(busRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> busService.deleteBus(2L));
    }
}
