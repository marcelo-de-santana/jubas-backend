package com.jubasbackend.service;

import com.jubasbackend.controller.request.FeedbackRequest;
import com.jubasbackend.controller.response.FeedbackResponse;
import com.jubasbackend.domain.entity.Feedback;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.FeedbackRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final MailService mailService;

    public List<FeedbackResponse> getFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(FeedbackResponse::new).toList();
    }

    public FeedbackResponse getFeedback(UUID feedbackId) {
        var feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Feedback not found."));

        return new FeedbackResponse(feedback);
    }

    public FeedbackResponse createFeedback(FeedbackRequest request) {

        var appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Appointment not found."));

        appointment.setAppointmentStatus(AppointmentStatus.AVALIADO);
        appointment.setUpdatedAt(Instant.now());
        appointmentRepository.save(appointment);

        var newFeedback = Feedback.builder()
                .id(appointment.getId())
                .appointment(appointment)
                .comment(request.comment())
                .rating(request.rating())
                .createdAt(Instant.now())
                .build();

        var savedFeedback = feedbackRepository.save(newFeedback);

        mailService.sendFeedback(
                savedFeedback.getAppointment().getEmployeeEmail(),
                savedFeedback.getAppointment().getClientName(),
                savedFeedback.getRating()
        );

        return new FeedbackResponse(savedFeedback);
    }
}
