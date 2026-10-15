package com.rental_vehicle.driver_service.controller;

import com.rental_vehicle.driver_service.entity.Driver;
import com.rental_vehicle.driver_service.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("drivers")
public class DriverController
{
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Driver addDriver(@Valid @RequestBody Driver driver)
    {
        return driverService.addDriver(driver);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Driver>> getAllDriver()
    {
        return ResponseEntity.ok(driverService.getAllDriver());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable("id") int id) {
        Driver driver = driverService.getDriverById(id);

        return ResponseEntity.ok(driver);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Driver> updateDriver(@PathVariable("id") int id,
                                               @Valid @RequestBody Driver updated)
    {
        return ResponseEntity.ok(driverService.updateDriver(id, updated));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteDriver(@PathVariable("id") int id)
    {
        return ResponseEntity.ok(driverService.deleteDriver(id));
    }
}
