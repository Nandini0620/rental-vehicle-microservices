package com.rental_vehicle.trip_service.service;

import com.rental_vehicle.trip_service.client.DriverClient;
import com.rental_vehicle.trip_service.client.VehicleClient;
import com.rental_vehicle.trip_service.dto.request.TripRequest;
import com.rental_vehicle.trip_service.entity.Trip;
import com.rental_vehicle.trip_service.entity.TripStatus;
import com.rental_vehicle.trip_service.exception.ResourceNotFoundException;
import com.rental_vehicle.trip_service.exception.ServiceUnavailableException;
import com.rental_vehicle.trip_service.repository.TripRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final VehicleClient vehicleClient;
    private final DriverClient driverClient;

    public TripService(TripRepository tripRepository, VehicleClient vehicleClient, DriverClient driverClient) {
        this.tripRepository = tripRepository;
        this.vehicleClient = vehicleClient;
        this.driverClient = driverClient;
    }

    public Trip addTrip(TripRequest tripRequest)
    {
        try
        {
            vehicleClient.getVehicleById(tripRequest.getVehicleId());
        }
        catch(FeignException.NotFound e)
        {
            throw new ResourceNotFoundException("Vehicle not found with id " + tripRequest.getVehicleId());
        }
        catch(FeignException e)
        {
            throw new ServiceUnavailableException("Vehicle service is currently unavailable");
        }

        try
        {
            driverClient.getDriverById(tripRequest.getDriverId());
        }
        catch(FeignException.NotFound e)
        {
            throw new ResourceNotFoundException("Driver not found with id " + tripRequest.getDriverId());
        }
        catch(FeignException e)
        {
            throw new ServiceUnavailableException("Driver service is currently unavailable");
        }

        Trip trip = new Trip();
        trip.setTitle(tripRequest.getTitle());
        trip.setRouteDetails(tripRequest.getRouteDetails());
        trip.setVehicleId(tripRequest.getVehicleId());
        trip.setDriverId(tripRequest.getDriverId());
        trip.setDueDate(tripRequest.getDueDate());
        trip.setStatus(TripStatus.SCHEDULED);

        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips()
    {
        return tripRepository.findAll();
    }

    public Trip getTripById(int id)
    {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + id));
    }

    public Trip updateTrip(int id, TripRequest tripRequest)
    {
        Trip trip = getTripById(id);
        trip.setTitle(tripRequest.getTitle());
        trip.setRouteDetails(tripRequest.getRouteDetails());
        trip.setDueDate(tripRequest.getDueDate());

        return tripRepository.save(trip);
    }

    public String deleteTrip(int id)
    {
        Trip trip = getTripById(id);
        tripRepository.delete(trip);
        return "Trip Deleted Successfully";
    }
}
