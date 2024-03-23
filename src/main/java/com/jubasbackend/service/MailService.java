package com.jubasbackend.service;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    public void sendEmail(@Email String to, String subject, String message) {
        var mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);
    }

    public void sendEmailToAdmin(String subject, String message) {
        var mail = new SimpleMailMessage();
        mail.setTo(mail.getFrom());
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);
    }

    public void sendCancellationEmails(
            String clientEmail,
            String employeeEmail,
            String clientMessage,
            String employeeMessage,
            String adminMessage) {
        final var SUBJECT = "Cancelamento de atendimento";
        sendEmail(clientEmail, SUBJECT, clientMessage);
        sendEmail(employeeEmail, SUBJECT, employeeMessage);
        sendEmailToAdmin(SUBJECT, adminMessage);
    }

}
