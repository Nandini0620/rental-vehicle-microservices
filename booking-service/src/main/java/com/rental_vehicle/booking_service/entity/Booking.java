package com.rental_vehicle.booking_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="bookings")
@Getter
@Setter
public class Booking
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Positive(message = "Customer id must be positive")
    private int customerId;

    @Positive(message = "Vehicle id must be positive")
    private int vehicleId;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date cannot be in the past")
    private LocalDate bookingDate;

    @Positive(message = "Duration must be at least 1 day")
    @Max(value = 30, message = "Duration cannot exceed 30 days")
    private int durationDays;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
