package com.rental_vehicle.driver_service.service;

import com.rental_vehicle.driver_service.entity.Driver;
import com.rental_vehicle.driver_service.exception.ResourceNotFoundException;
import com.rental_vehicle.driver_service.repository.DriverRepository;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService
{
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver addDriver(@Valid Driver driver)
    {
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDriver()
    {
        return driverRepository.findAll();
    }

    public Driver getDriverById(int id)
    {
        return driverRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id " + id));
    }

    public Driver updateDriver(int id, @Valid Driver updated) {
        Driver existing = getDriverById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setLicenseNumber(updated.getLicenseNumber());
        existing.setSpecialization(updated.getSpecialization());
        return driverRepository.save(existing);
    }

    public String deleteDriver(int id)
    {
        Driver driver=getDriverById(id);

        driverRepository.delete(driver);
        return "Driver Deleted Sucessfully";
    }
}
