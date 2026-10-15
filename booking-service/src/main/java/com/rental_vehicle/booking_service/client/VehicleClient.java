package com.rental_vehicle.booking_service.client;

import com.rental_vehicle.booking_service.dto.VehicleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-service")
public interface VehicleClient
{
    @GetMapping("/vehicles/get/{id}")
    public VehicleDto getVehicleById(@PathVariable("id") int id);
}
