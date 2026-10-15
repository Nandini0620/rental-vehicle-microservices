package com.rental_vehicle.feedback_service.client;

import com.rental_vehicle.feedback_service.dto.TripDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "trip-service")
public interface TripClient {

    @GetMapping("/trips/get/{id}")
    TripDto getTripById(@PathVariable("id") int id);
}
