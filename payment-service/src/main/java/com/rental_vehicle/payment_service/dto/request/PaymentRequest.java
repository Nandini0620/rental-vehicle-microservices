package com.rental_vehicle.payment_service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest
{
    @Positive(message = "Customer id must be positive")
    private int customerId;

    @Positive(message = "Booking id must be positive")
    private int bookingId;

    @Positive(message = "Amount must be greater than 0")
    @Max(value = 1000000, message = "Amount cannot exceed 10,00,000")
    private double amount;
}
