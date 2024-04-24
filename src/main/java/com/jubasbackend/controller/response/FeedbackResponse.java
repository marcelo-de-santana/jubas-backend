package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Feedback;
import com.jubasbackend.domain.entity.enums.Rating;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FeedbackResponse extends AppointmentResponse {

    final Instant createdAt;
    final String comment;
    final Rating rating;

    public FeedbackResponse(Feedback feedback) {

        super(feedback.getAppointment());
        this.createdAt = feedback.getCreatedAt();
        this.comment = feedback.getComment();
        this.rating = feedback.getRating();

    }

}
