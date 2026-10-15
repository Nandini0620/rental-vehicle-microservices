package com.rental_vehicle.trip_service.repository;

import com.rental_vehicle.trip_service.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
}
