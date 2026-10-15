package com.rental_vehicle.driver_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "drivers")
@Getter
@Setter
public class Driver
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank
    @Column(unique = true)
    @Size(min = 8, max = 20)
    private String licenseNumber;
    @NotNull
    @Enumerated(EnumType.STRING)
    private DriverSpecialization specialization;
}
