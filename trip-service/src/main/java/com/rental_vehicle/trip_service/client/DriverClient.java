package com.rental_vehicle.trip_service.client;

import com.rental_vehicle.trip_service.dto.DriverDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver-service")
public interface DriverClient {

    @GetMapping("/drivers/get/{id}")
    DriverDto getDriverById(@PathVariable("id") int id);
}
