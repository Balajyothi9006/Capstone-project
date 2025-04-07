package com.example.onlineBusBookingdemo.controller;

import com.example.onlineBusBookingdemo.Entity.Bus;
import com.example.onlineBusBookingdemo.Service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/buses")
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping("/search")
    public String showSearchForm() {
        return "search-buses";
    }

    @PostMapping("/search")
    public String searchBuses(@RequestParam String fromDestination, @RequestParam String toDestination,@RequestParam int userId, Model model) {
        List<Bus> buses = busService.searchBuses(fromDestination,toDestination);
        model.addAttribute("buses", buses);
        model.addAttribute("userId", userId);
        return "search-result";
    }

    @GetMapping("/add")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        return "admin/add-bus";
    }

    @PostMapping("/add")
    public String addBus(@ModelAttribute Bus bus) {
        busService.addBus(bus);
        return "redirect:/buses/all";
    }

    @GetMapping("/all")
    public String listBuses(Model model) {
        model.addAttribute("buses", busService.getAllBuses());
        return "all-buses";
    }

    @GetMapping("/delete/{id}")
    public String deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return "redirect:/buses/all";
    }
}
