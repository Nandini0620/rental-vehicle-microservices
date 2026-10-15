package com.rental_vehicle.feedback_service.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class FeedbackRequest
{
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
    private String comments;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
