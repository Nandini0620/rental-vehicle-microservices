package com.rental_vehicle.trip_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "trips")
@Getter
@Setter
public class Trip
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Route details are required")
    @Size(max = 500, message = "Route details cannot exceed 500 characters")
    @Column(length = 500)
    private String routeDetails;

    @Positive(message = "Vehicle id must be positive")
    private int vehicleId;

    @Positive(message = "Driver id must be positive")
    private int driverId;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TripStatus status;
}
