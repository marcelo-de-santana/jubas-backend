package com.jubasbackend.service;

import com.jubasbackend.controller.request.MailRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;

    public void sendEmail(MailRequest request) throws MessagingException {
        var message = javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);

//        var body = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        javaMailSender.send(message);
    }
}
