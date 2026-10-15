package com.rental_vehicle.feedback_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Positive(message = "Customer id must be positive")
    private int customerId;

    @Positive(message = "Trip id must be positive")
    private int tripId;

    @Positive(message = "Booking id must be positive")
    private int bookingId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private int ratingScore;

    @Size(max = 500, message = "Comments cannot exceed 500 characters")
    @Column(length = 500)
    private String comments;

    private LocalDate feedbackDate;

}
