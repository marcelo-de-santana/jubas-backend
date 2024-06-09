package com.jubasbackend.service;

import com.jubasbackend.controller.request.MailRequest;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.enums.PaymentMethod;
import com.jubasbackend.domain.entity.enums.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class MailService {

    @Autowired
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public void sendEmail(MailRequest request) {
        var mail = new SimpleMailMessage();
        mail.setTo(request.to());
        mail.setSubject(request.subject());
        mail.setText(request.message());
        mailSender.send(mail);
    }

    public void sendAppointment(
            String clientEmail, String employeeName, LocalDate date, LocalTime time, AppointmentStatus appointmentStatus) {

        var formattedStatus = replaceUnderscoreWithSpace(
                capitalizeFirstLetter(appointmentStatus.toString().toLowerCase())
        );

        var formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        final var CLIENT_MESSAGE = String.format("""
                Dia: %s
                Horário: %s
                Funcionário: %s
                Status: %s
                """, formattedDate, time, employeeName, formattedStatus);

        sendEmail(new MailRequest(clientEmail, "Atendimento", CLIENT_MESSAGE));

    }

    public void sendFeedback(String employeeEmail, String clientName, Rating rating, String comment) {
        var message = clientName + " deixou uma avaliação sobre o seu atendimento.\nNota: "
                      + formatAndCapitalize(rating.toString()) + "\nComentário: \"" + comment + "\"";

        sendEmail(new MailRequest(employeeEmail, "Avaliação do atendimento", message));
    }


    public void sendPaymentOnSuccess(String clientName, String employeeName, LocalDateTime dateTime, PaymentMethod method, Long transactionId) {
        final var transactionInfo = "\nNúmero da transação: " + transactionId;
        final var message = buildPaymentMessage(clientName, employeeName, dateTime, method, true) + transactionInfo;

        sendEmail(new MailRequest(mailUsername, "Confirmação de Pagamento", message));
    }

    public void sendPaymentOnError(String clientName, String employeeName, LocalDateTime dateTime, PaymentMethod method) {
        var message = buildPaymentMessage(clientName, employeeName, dateTime, method, false);
        sendEmail(new MailRequest(mailUsername, "Erro no Pagamento", message));
    }

    private String buildPaymentMessage(String clientName, String employeeName, LocalDateTime dateTime, PaymentMethod method, boolean isSuccess) {
        var time = dateTime.toLocalTime();
        var formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        var formattedMethod = formatAndCapitalize(method.toString());

        if (method.equals(PaymentMethod.CREDITO) || method.equals(PaymentMethod.DEBITO)) {
            formattedMethod = "Cartão de " + formattedMethod;
        }

        var status = isSuccess ? "foi recebido com sucesso" : "foi rejeitado";
        return String.format("""
                O pagamento pelo atendimento %s.
                Cliente: %s
                Funcionário: %s
                Método: %s
                Data do atendimento: %s às %sh.""", status, clientName, employeeName, formattedMethod, formattedDate, time);
    }

    private String formatAndCapitalize(String text) {
        return replaceUnderscoreWithSpace(capitalizeFirstLetter(text.toLowerCase()));
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    private String replaceUnderscoreWithSpace(String input) {
        if (input == null) {
            return null;
        }
        return input.replace('_', ' ');
    }

}
