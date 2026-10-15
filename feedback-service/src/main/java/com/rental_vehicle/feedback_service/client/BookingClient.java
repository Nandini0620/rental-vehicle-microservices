package com.rental_vehicle.feedback_service.client;

import com.rental_vehicle.feedback_service.dto.BookingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/bookings/get/{id}")
    BookingDto getBookingById(@PathVariable("id") int id);
}