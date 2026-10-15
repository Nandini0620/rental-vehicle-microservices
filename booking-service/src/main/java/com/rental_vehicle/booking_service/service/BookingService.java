package com.rental_vehicle.booking_service.service;

import com.rental_vehicle.booking_service.client.CustomerClient;
import com.rental_vehicle.booking_service.client.VehicleClient;
import com.rental_vehicle.booking_service.dto.request.BookingRequest;
import com.rental_vehicle.booking_service.entity.Booking;
import com.rental_vehicle.booking_service.entity.BookingStatus;
import com.rental_vehicle.booking_service.exception.ResourceNotFoundException;
import com.rental_vehicle.booking_service.exception.ServiceUnavailableException;
import com.rental_vehicle.booking_service.repository.BookingRepository;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService
{
    private final BookingRepository bookingRepository;
    private final CustomerClient customerClient;
    private final VehicleClient vehicleClient;

    public BookingService(BookingRepository bookingRepository,
                          CustomerClient customerClient,
                          VehicleClient vehicleClient) {
        this.bookingRepository = bookingRepository;
        this.customerClient = customerClient;
        this.vehicleClient = vehicleClient;
    }

    public Booking addBooking(BookingRequest bookingRequest)
    {
        try
        {
            customerClient.getCustomerByID(bookingRequest.getCustomerId());
        }
        catch(FeignException.NotFound e)
        {
            throw new ResourceNotFoundException("Customer not found with id " + bookingRequest.getCustomerId());
        }
        catch(FeignException e)
        {
            throw new ServiceUnavailableException("Customer service is currently unavaible");
        }

        try
        {
            vehicleClient.getVehicleById(bookingRequest.getVehicleId());
        }
        catch(FeignException.NotFound e)
        {
            throw new ResourceNotFoundException("Vehical not found with id " + bookingRequest.getVehicleId());
        }
        catch(FeignException e)
        {
            throw new ServiceUnavailableException("Vehicle service is currently unavaible");
        }

        Booking booking=new Booking();
        booking.setCustomerId(bookingRequest.getCustomerId());
        booking.setVehicleId(bookingRequest.getVehicleId());
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setDurationDays(bookingRequest.getDurationDays());
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }


    public List<Booking> getAllBooking()
    {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(int id)
    {
        return bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking Not Found with Id : "+ id));
    }

    public Booking updateBooking(int id, BookingRequest bookingRequest)
    {
        Booking booking= getBookingById(id);
        booking.setCustomerId(bookingRequest.getCustomerId());
        booking.setVehicleId(bookingRequest.getVehicleId());
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setDurationDays(bookingRequest.getDurationDays());
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    public String deleteBooking(int id)
    {
        Booking booking= getBookingById(id);
        bookingRepository.delete(booking);
        return "Booking Deleted Successfully";
    }
}
