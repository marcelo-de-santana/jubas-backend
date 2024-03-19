package com.jubasbackend.core.mail;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendEmail(MailRequest request) throws MessagingException;
}
