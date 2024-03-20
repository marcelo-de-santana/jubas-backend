package com.jubasbackend.controller.request;

public record MailRequest(String to, String subject, String message) {
}
