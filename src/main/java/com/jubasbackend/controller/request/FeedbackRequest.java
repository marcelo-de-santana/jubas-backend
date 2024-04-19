package com.jubasbackend.controller.request;

import com.jubasbackend.domain.entity.enums.Rating;

import java.util.UUID;

public record FeedbackRequest(UUID appointmentId, String comment, Rating rating) {
}
