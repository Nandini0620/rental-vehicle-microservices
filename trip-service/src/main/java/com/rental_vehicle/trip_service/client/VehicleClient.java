package com.rental_vehicle.trip_service.client;

import com.rental_vehicle.trip_service.dto.VehicleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {

    @GetMapping("/vehicles/get/{id}")
    VehicleDto getVehicleById(@PathVariable("id") int id);
}