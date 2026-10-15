package com.rental_vehicle.trip_service.controller;

import com.rental_vehicle.trip_service.dto.request.TripRequest;
import com.rental_vehicle.trip_service.entity.Trip;
import com.rental_vehicle.trip_service.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService service;

    public TripController(TripService service) {
        this.service = service;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Trip createTrip(@Valid @RequestBody TripRequest trip) {
        return service.addTrip(trip);
    }

    @GetMapping("/get/{id}")
    public Trip getTrip(@PathVariable("id") int id) {
        return service.getTripById(id);
    }

    @GetMapping("/get")
    public List<Trip> getAllTrips() {
        return service.getAllTrips();
    }

    @PutMapping("/update/{id}")
    public Trip updateTrip(@PathVariable("id") int id, @Valid @RequestBody TripRequest trip) {
        return service.updateTrip(id, trip);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTrip(@PathVariable("id") int id) {
        return service.deleteTrip(id);
    }
}
