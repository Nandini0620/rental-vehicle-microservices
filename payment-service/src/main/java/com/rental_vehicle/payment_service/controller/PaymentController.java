package com.rental_vehicle.payment_service.controller;

import com.rental_vehicle.payment_service.dto.request.PaymentRequest;
import com.rental_vehicle.payment_service.entity.Payment;
import com.rental_vehicle.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("payments")
public class PaymentController
{
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Payment addBooking(@Valid @RequestBody PaymentRequest paymentRequest)
    {
        return paymentService.addPayment(paymentRequest);
    }

    @GetMapping("/get")
    public List<Payment> getAllPayment()
    {
        return paymentService.getAllPayment();
    }

    @GetMapping("/get/{id}")
    public Payment getPaymentById(@PathVariable("id") int id)
    {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/update/{id}")
    public Payment updatePayment(@PathVariable("id") int id,
                                 @Valid @RequestBody PaymentRequest paymentRequest)
    {
        return paymentService.updatePayment(id,paymentRequest);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePayment(@PathVariable("id") int id)
    {
        return paymentService.deletePayment(id);
    }
}
