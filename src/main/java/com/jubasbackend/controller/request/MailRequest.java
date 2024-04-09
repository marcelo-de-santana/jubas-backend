package com.jubasbackend.controller.request;

import jakarta.validation.constraints.Email;

public record MailRequest(@Email String to, String subject, String message) {
}
