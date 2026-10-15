package com.rental_vehicle.feedback_service.service;

import com.rental_vehicle.feedback_service.client.BookingClient;
import com.rental_vehicle.feedback_service.client.CustomerClient;
import com.rental_vehicle.feedback_service.client.TripClient;
import com.rental_vehicle.feedback_service.dto.BookingDto;
import com.rental_vehicle.feedback_service.dto.request.FeedbackRequest;
import com.rental_vehicle.feedback_service.entity.Feedback;
import com.rental_vehicle.feedback_service.exception.InvalidRequestException;
import com.rental_vehicle.feedback_service.exception.ResourceNotFoundException;
import com.rental_vehicle.feedback_service.exception.ServiceUnavailableException;
import com.rental_vehicle.feedback_service.repository.FeedbackRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final CustomerClient customerClient;
    private final TripClient tripClient;
    private final BookingClient bookingClient;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           CustomerClient customerClient,
                           TripClient tripClient,
                           BookingClient bookingClient) {
        this.feedbackRepository = feedbackRepository;
        this.customerClient = customerClient;
        this.tripClient = tripClient;
        this.bookingClient = bookingClient;
    }

    public Feedback addFeedback(FeedbackRequest feedbackRequest) {
        try {
            customerClient.getCustomerById(feedbackRequest.getCustomerId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Customer not found with id " + feedbackRequest.getCustomerId());
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Customer service is currently unavailable");
        }

        try {
            tripClient.getTripById(feedbackRequest.getTripId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Trip not found with id " + feedbackRequest.getTripId());
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Trip service is currently unavailable");
        }

        BookingDto booking;
        try {
            booking = bookingClient.getBookingById(feedbackRequest.getBookingId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Booking not found with id " + feedbackRequest.getBookingId());
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Booking service is currently unavailable");
        }

        if (booking.getCustomerId() != feedbackRequest.getCustomerId()) {
            throw new InvalidRequestException("Booking does not belong to this customer");
        }

        Feedback feedback = new Feedback();
        feedback.setCustomerId(feedbackRequest.getCustomerId());
        feedback.setTripId(feedbackRequest.getTripId());
        feedback.setBookingId(feedbackRequest.getBookingId());
        feedback.setRatingScore(feedbackRequest.getRatingScore());
        feedback.setComments(feedbackRequest.getComments());
        feedback.setFeedbackDate(LocalDate.now());

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(int id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id " + id));
    }

    public Feedback updateFeedback(int id, FeedbackRequest feedbackRequest) {
        Feedback feedback = getFeedbackById(id);
        feedback.setRatingScore(feedbackRequest.getRatingScore());
        feedback.setComments(feedbackRequest.getComments());

        return feedbackRepository.save(feedback);
    }

    public String deleteFeedback(int id) {
        Feedback feedback = getFeedbackById(id);
        feedbackRepository.delete(feedback);
        return "Feedback Deleted Successfully";
    }
}
