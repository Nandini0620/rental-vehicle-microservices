package com.rental_vehicle.customer_service.service;

import com.rental_vehicle.customer_service.entity.Customer;
import com.rental_vehicle.customer_service.exception.ResourceNotFoundException;
import com.rental_vehicle.customer_service.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService
{
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer)
    {
        return customerRepository.save(customer);
    }

    public  List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(int id)
    {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    public Customer updateCustomer(int id, Customer updated)
    {
        Customer existing = getCustomerById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setMobile(updated.getMobile());
        return customerRepository.save(existing);
    }

    public String deleteCustomer(int id)
    {
        Customer customer=getCustomerById(id);
        customerRepository.delete(customer);

        return "Customer Deleted Successfully";

    }
}
