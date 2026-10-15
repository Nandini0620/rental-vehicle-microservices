package com.rental_vehicle.booking_service.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BookingRequest
{
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
}
