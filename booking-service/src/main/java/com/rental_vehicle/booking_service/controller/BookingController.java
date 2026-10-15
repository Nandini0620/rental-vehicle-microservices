package com.rental_vehicle.booking_service.controller;

import com.rental_vehicle.booking_service.client.CustomerClient;
import com.rental_vehicle.booking_service.client.VehicleClient;
import com.rental_vehicle.booking_service.dto.CustomerDto;
import com.rental_vehicle.booking_service.dto.VehicleDto;
import com.rental_vehicle.booking_service.dto.request.BookingRequest;
import com.rental_vehicle.booking_service.entity.Booking;
import com.rental_vehicle.booking_service.service.BookingService;
import jakarta.validation.Valid;
import org.aspectj.lang.annotation.DeclareError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingController
{
    private final BookingService bookingService;


    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;

    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Booking addBooking(@Valid @RequestBody BookingRequest bookingRequest)
    {
        return bookingService.addBooking(bookingRequest);
    }

    @GetMapping("/get")
    public List<Booking> getAllBooking()
    {
        return bookingService.getAllBooking();
    }

    @GetMapping("/get/{id}")
    public Booking getBookingById(@PathVariable("id") int id)
    {
        return bookingService.getBookingById(id);
    }

    @PutMapping("/update/{id}")
    public Booking updateBooking(@PathVariable("id") int id,
                                 @Valid @RequestBody BookingRequest bookingRequest)
    {
        return bookingService.updateBooking(id,bookingRequest);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") int id)
    {
        return bookingService.deleteBooking(id);
    }

}
