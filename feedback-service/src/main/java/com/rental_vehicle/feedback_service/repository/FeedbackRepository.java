package com.rental_vehicle.feedback_service.repository;

import com.rental_vehicle.feedback_service.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer>
{

}
