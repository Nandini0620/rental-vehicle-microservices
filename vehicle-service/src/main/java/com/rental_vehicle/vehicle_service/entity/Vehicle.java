package com.rental_vehicle.vehicle_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
public class Vehicle
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 100)
    private String vehicleName;
    @NotBlank
    private String model;
    @NotNull(message = "Vehicle type is required")
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    @Positive
    private double dailyFee;
}
