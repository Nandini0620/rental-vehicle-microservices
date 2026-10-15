package com.rental_vehicle.vehicle_service.controller;

import com.rental_vehicle.vehicle_service.entity.Vehicle;
import com.rental_vehicle.vehicle_service.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vehicles")
public class VehicleController
{
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle createVehicle(@Valid @RequestBody Vehicle vehicle)
    {
        return vehicleService.createVehicle(vehicle);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Vehicle>> getAllVehicle()
    {
        return ResponseEntity.ok(vehicleService.getAllVehicle());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable("id") int id) {


        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Vehicle> updateCustomer(@PathVariable("id") int id,
                                                   @Valid @RequestBody Vehicle updated)
    {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, updated));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id)
    {
        return ResponseEntity.ok(vehicleService.deleteVehicle(id));
    }
}
