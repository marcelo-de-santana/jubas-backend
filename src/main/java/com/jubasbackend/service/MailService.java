package com.jubasbackend.service;

import com.jubasbackend.controller.request.MailRequest;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.enums.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String adminEmail;
    private final MailSender mailSender;

    public void sendEmail(MailRequest request) {
        var mail = new SimpleMailMessage();
        mail.setTo(request.to());
        mail.setSubject(request.subject());
        mail.setText(request.message());
        mailSender.send(mail);
    }

    public void sendEmailToAdmin(String subject, String message) {
        var mail = new SimpleMailMessage();
        mail.setTo(adminEmail);
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);
    }

    public void sendAppointment(String clientEmail, String clientName, String employeeEmail, String employeeName,
                                LocalDate date, LocalTime time, AppointmentStatus appointmentStatus) {

        var statusCapitalized = capitalizeFirstLetter(appointmentStatus.toString().toLowerCase());

        var formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        final var SUBJECT = "Atendimento";

        final var CLIENT_MESSAGE = String.format("""
                Dia: %s
                Horário: %s
                Funcionário: %s
                Status: %s
                """, formattedDate, time, employeeName, statusCapitalized);

        final var EMPLOYEE_MESSAGE = String.format("""
                Dia: %s
                Horário: %s
                Cliente: %s
                Status: %s
                """, formattedDate, time, clientName, statusCapitalized);

        final var ADMIN_MESSAGE = String.format("""
                Dia: %s
                Horário: %s
                Funcionário: %s
                Cliente: %s
                Status: %s
                """, formattedDate, time, employeeName, clientName, statusCapitalized);

        sendEmail(new MailRequest(clientEmail, SUBJECT, CLIENT_MESSAGE));
        sendEmail(new MailRequest(employeeEmail, SUBJECT, EMPLOYEE_MESSAGE));
        sendEmailToAdmin(SUBJECT, ADMIN_MESSAGE);
    }

    public void sendFeedback(String employeeEmail, String employeeName, String clientName, Rating rating) {

        final var SUBJECT = "Avaliação do atendimento";
        final var capitalizedRating = capitalizeFirstLetter(rating.toString().toLowerCase());

        final var employeeMessage = String.format("""
                %s avaliou o atendimento como %s.
                """, clientName, capitalizedRating
        );

        var adminMessage = String.format("""
                %s avaliou o atendimento de %s como %s.
                """, clientName, employeeName, capitalizedRating
        );

        sendEmail(new MailRequest(employeeEmail, SUBJECT, employeeMessage));
        sendEmailToAdmin(SUBJECT, adminMessage);
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

}
