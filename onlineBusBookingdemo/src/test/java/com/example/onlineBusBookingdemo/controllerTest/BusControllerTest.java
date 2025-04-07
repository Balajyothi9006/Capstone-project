package com.example.onlineBusBookingdemo.controllerTest;


import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Service.BusService;
import com.example.onlineBusBookingdemo.controller.BusController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(BusController.class)
public class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusService busService;

    private Bus bus;

    @BeforeEach
    void setup() {
        bus = new Bus();
        bus.setId(1L);
        bus.setName("Test Bus");
        bus.setSource("City A");
        bus.setDestination("City B");
        bus.setTravelDate(LocalDate.of(2025, 4, 10));
        bus.setDepartureTime(LocalTime.of(10, 0));
        bus.setArrivalTime(LocalTime.of(14, 0));
        bus.setTotalSeats(40);
        bus.setPricePerSeat(500);
    }

    @Test
    void testShowSearchForm() throws Exception {
        mockMvc.perform(get("/buses/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-buses"));
    }

    @Test
    void testSearchBuses() throws Exception {
        List<Bus> buses = Arrays.asList(bus);

        when(busService.searchBuses("City A", "City B")).thenReturn(buses);

        mockMvc.perform(post("/buses/search")
                        .param("fromDestination", "City A")
                        .param("toDestination", "City B")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("buses"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(view().name("search-result"));
    }

    @Test
    void testShowAddBusForm() throws Exception {
        mockMvc.perform(get("/buses/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bus"))
                .andExpect(view().name("admin/add-bus"));
    }

    @Test
    void testAddBus() throws Exception {
        mockMvc.perform(post("/buses/add")
                        .flashAttr("bus", bus))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buses/all"));

        verify(busService, times(1)).addBus(any(Bus.class));
    }

    @Test
    void testListBuses() throws Exception {
        when(busService.getAllBuses()).thenReturn(Arrays.asList(bus));

        mockMvc.perform(get("/buses/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("buses"))
                .andExpect(view().name("all-buses"));
    }

    @Test
    void testDeleteBus() throws Exception {
        mockMvc.perform(get("/buses/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buses/all"));

        verify(busService, times(1)).deleteBus(1L);
    }
}

