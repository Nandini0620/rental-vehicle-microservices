package com.rental_vehicle.payment_service.service;

import com.rental_vehicle.payment_service.client.BookingClient;
import com.rental_vehicle.payment_service.client.CustomerClient;
import com.rental_vehicle.payment_service.dto.request.PaymentRequest;
import com.rental_vehicle.payment_service.entity.Payment;
import com.rental_vehicle.payment_service.entity.PaymentStatus;
import com.rental_vehicle.payment_service.exception.ResourceNotFoundException;
import com.rental_vehicle.payment_service.repository.PaymentRepository;
import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.ws.rs.ServiceUnavailableException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerClient customerClient;
    private final BookingClient bookingClient;

    public PaymentService(PaymentRepository paymentRepository,
                          CustomerClient customerClient,
                          BookingClient bookingClient) {
        this.paymentRepository = paymentRepository;
        this.customerClient = customerClient;
        this.bookingClient = bookingClient;
    }

    public Payment addPayment(@Valid PaymentRequest paymentRequest)
    {
        try
        {
            customerClient.getCustomerByID(paymentRequest.getCustomerId());
        }
        catch(FeignException.NotFound e)
        {
            throw new ResourceNotFoundException("Customer not found with id " + paymentRequest.getCustomerId());
        }
        catch(FeignException e)
        {
            throw new ServiceUnavailableException("Customer service is currently unavaible");
        }

        try
        {
            bookingClient.getBookingByID(paymentRequest.getBookingId());
        }
        catch(FeignException.NotFound e)
        {
            throw new ResourceNotFoundException("Booking not found with id " + paymentRequest.getBookingId());
        }
        catch(FeignException e)
        {
            throw new ServiceUnavailableException("Booking service is currently unavaible");
        }
        Payment payment=new Payment();
        payment.setCustomerId(paymentRequest.getCustomerId());
        payment.setBookingId(paymentRequest.getBookingId());
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(payment.getAmount());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayment()
    {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(int id)
    {
        return paymentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Payment Not Found with Id : "+ id));
    }

    public Payment updatePayment(int id, @Valid PaymentRequest paymentRequest)
    {
        Payment payment=getPaymentById(id);
        payment.setCustomerId(paymentRequest.getCustomerId());
        payment.setBookingId(paymentRequest.getBookingId());
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(payment.getAmount());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepository.save(payment);
    }

    public String deletePayment(int id)
    {
        Payment payment=getPaymentById(id);
        paymentRepository.delete(payment);
        return "Payment History Deleted Successfully";
    }
}
