package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Feedback;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.enums.Rating;

import java.time.Instant;
import java.util.UUID;

public record FeedbackResponse(
        UUID appointmentId,
        GenericDTO employee,
        GenericDTO client,
        SpecialtyResponse specialty,
        Instant createdAt,
        String comment,
        Rating rating
) {

    public FeedbackResponse(Feedback feedback) {
        this(
                feedback.getId(),
                new GenericDTO(feedback.getAppointment().getEmployee()),
                new GenericDTO(feedback.getAppointment().getClient()),
                new SpecialtyResponse(feedback.getAppointment().getSpecialty()),
                feedback.getCreatedAt(),
                feedback.getComment(),
                feedback.getRating()
        );
    }

    record GenericDTO(UUID id, String name) {
        GenericDTO(Employee entity) {
            this(
                    entity.getId(),
                    entity.getProfile().getName()
            );
        }

        GenericDTO(Profile entity) {
            this(
                    entity.getId(),
                    entity.getName()
            );
        }

    }
}
