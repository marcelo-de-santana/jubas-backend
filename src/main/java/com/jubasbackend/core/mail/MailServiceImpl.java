package com.jubasbackend.core.mail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(MailRequest request) throws MessagingException {
        var message = javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);

//        var body = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        javaMailSender.send(message);
    }
}
