package com.rental_vehicle.driver_service.repository;

import com.rental_vehicle.driver_service.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

}
