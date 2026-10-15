package com.rental_vehicle.customer_service.controller;

import com.rental_vehicle.customer_service.entity.Customer;
import com.rental_vehicle.customer_service.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController
{
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer)
    {
        Customer c=customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Customer>> getAllCustomer()
    {
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") int id) {
        Customer customer = customerService.getCustomerById(id);

        return ResponseEntity.ok(customer);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") int id,
                                                   @Valid @RequestBody Customer updated)
    {
        return ResponseEntity.ok(customerService.updateCustomer(id, updated));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") int id)
    {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
