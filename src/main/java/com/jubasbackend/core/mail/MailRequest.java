package com.jubasbackend.core.mail;

public record MailRequest(String to, String subject, String message) {
}
