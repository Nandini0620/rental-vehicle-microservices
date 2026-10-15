package com.rental_vehicle.payment_service.client;

import com.rental_vehicle.payment_service.dto.BookingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service")
public interface BookingClient
{
    @GetMapping("/bookings/get/{id}")
    public BookingDto getBookingByID(@PathVariable("id") int id);
}
