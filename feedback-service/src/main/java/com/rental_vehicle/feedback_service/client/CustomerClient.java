package com.rental_vehicle.feedback_service.client;

import com.rental_vehicle.feedback_service.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/customers/get/{id}")
    CustomerDto getCustomerById(@PathVariable("id") int id);
}
