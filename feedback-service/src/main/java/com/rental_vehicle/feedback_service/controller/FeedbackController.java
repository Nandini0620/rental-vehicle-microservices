package com.rental_vehicle.feedback_service.controller;

import com.rental_vehicle.feedback_service.dto.request.FeedbackRequest;
import com.rental_vehicle.feedback_service.entity.Feedback;
import com.rental_vehicle.feedback_service.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Feedback createFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        return feedbackService.addFeedback(feedbackRequest);
    }

    @GetMapping("/{id}")
    public Feedback getFeedback(@PathVariable("id") int id) {
        return feedbackService.getFeedbackById(id);
    }

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @PutMapping("/update/{id}")
    public Feedback updateFeedback(@PathVariable("id") int id, @Valid @RequestBody FeedbackRequest feedback) {
        return feedbackService.updateFeedback(id, feedback);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable("id") int id) {
        return feedbackService.deleteFeedback(id);
    }
}
