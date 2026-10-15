package com.rental_vehicle.vehicle_service.service;

import com.rental_vehicle.vehicle_service.entity.Vehicle;
import com.rental_vehicle.vehicle_service.exception.ResourceNotFoundException;
import com.rental_vehicle.vehicle_service.repository.VehicleRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(@Valid Vehicle vehicle)
    {
        return vehicleRepository.save(vehicle);
    }


    public List<Vehicle> getAllVehicle()
    {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(int id)
    {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));
    }

    public Vehicle updateVehicle(int id, @Valid Vehicle updated)
    {
        Vehicle existing= getVehicleById(id);
        existing.setVehicleName(updated.getVehicleName());
        existing.setModel(updated.getModel());
        existing.setType(updated.getType());
        existing.setDailyFee(updated.getDailyFee());
        return vehicleRepository.save(existing);

    }

    public String deleteVehicle(int id)
    {
        Vehicle vehicle=getVehicleById(id);
        vehicleRepository.delete(vehicle);
        return "Vehicle Deleted Successfully";
    }
}
