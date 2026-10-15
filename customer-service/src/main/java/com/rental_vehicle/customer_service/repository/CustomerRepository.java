package com.rental_vehicle.customer_service.repository;

import com.rental_vehicle.customer_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
