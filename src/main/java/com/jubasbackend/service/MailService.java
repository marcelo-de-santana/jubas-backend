package com.jubasbackend.service;

import com.jubasbackend.controller.request.MailRequest;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.enums.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    public void sendEmail(MailRequest request) {
        var mail = new SimpleMailMessage();
        mail.setTo(request.to());
        mail.setSubject(request.subject());
        mail.setText(request.message());
        mailSender.send(mail);
    }

    public void sendAppointment(
            String clientEmail, String employeeName, LocalDate date, LocalTime time, AppointmentStatus appointmentStatus) {

        var statusCapitalized = capitalizeFirstLetter(appointmentStatus.toString().toLowerCase());

        var formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        final var SUBJECT = "Atendimento";

        final var CLIENT_MESSAGE = String.format("""
                Dia: %s
                Horário: %s
                Funcionário: %s
                Status: %s
                """, formattedDate, time, employeeName, statusCapitalized);

        sendEmail(new MailRequest(clientEmail, SUBJECT, CLIENT_MESSAGE));

    }

    public void sendFeedback(String employeeEmail, String clientName, Rating rating) {

        final var SUBJECT = "Avaliação do atendimento";
        final var capitalizedRating = capitalizeFirstLetter(rating.toString().toLowerCase());

        final var employeeMessage = String.format("""
                %s avaliou o atendimento como %s.
                """, clientName, capitalizedRating
        );

        sendEmail(new MailRequest(employeeEmail, SUBJECT, employeeMessage));
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

}
