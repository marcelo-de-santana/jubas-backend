package com.jubasbackend.service;

import com.jubasbackend.controller.request.PaymentRequest;
import com.jubasbackend.controller.response.MercadoPagoTokenResponse;
import com.jubasbackend.controller.response.PaymentResponse;
import com.jubasbackend.domain.entity.Payment;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.enums.PaymentMethod;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.PaymentRepository;
import com.jubasbackend.exception.APIException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${mercado-pago.access_token}")
    private String accessToken;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MailService mailService;

    public Payment createPayment(PaymentRequest request) {

        var appointment = appointmentRepository.findAppointment(request.appointmentId());

        if (request.paymentMethod().equals(PaymentMethod.DINHEIRO)) {
            appointment.setAppointmentStatus(AppointmentStatus.PAGO);
            appointment.setUpdatedAt(Instant.now());

            appointment = appointmentRepository.save(appointment);

            var payment = Payment.builder()
                    .id(appointment.getId())
                    .appointment(appointment)
                    .method(request.paymentMethod())
                    .build();

            return paymentRepository.save(payment);
        }


        var customHeaders = new HashMap<String, String>();
        customHeaders.put("x-idempotency-key", request.appointmentId().toString());

        var requestOptions = MPRequestOptions.builder()
                .customHeaders(customHeaders)
                .build();

        MercadoPagoConfig.setAccessToken(accessToken);

        PaymentClient client = new PaymentClient();

        var paymentDescription = appointment.getSpecialty().getName()
                + " com " + appointment.getEmployeeName()
                + " em " + appointment.getDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        var paymentCreateRequest = PaymentCreateRequest.builder()
                .description(paymentDescription)
                .token(request.cardToken())
                .transactionAmount(appointment.getSpecialty().getPrice())
                .installments(1)
                .paymentMethodId(request.paymentMethodId())
                .binaryMode(false)
                .capture(false)
                .threeDSecureMode("optional")
                .payer(
                        PaymentPayerRequest.builder()
                                .email(appointment.getClientEmail())
                                .identification(
                                        IdentificationRequest.builder()
                                                .type(request.identificationType())
                                                .number(request.identificationNumber())
                                                .build()
                                )
                                .firstName(appointment.getClientName())
                                .build())
                .build();

        try {
            var response = client.create(paymentCreateRequest, requestOptions);

            if (!response.getStatus().equals(PaymentStatus.AUTHORIZED)) {
                mailService.sendPaymentOnError(
                        appointment.getClientName(), appointment.getEmployeeName(),
                        appointment.getDate(), request.paymentMethod()
                );

                throw new APIException(HttpStatus.UNPROCESSABLE_ENTITY, "Não foi possível realizar o pagamentos");
            }

            mailService.sendPaymentOnSuccess(appointment.getClientName(), appointment.getEmployeeName(),
                    appointment.getDate(), request.paymentMethod(), response.getId());

            appointment.setAppointmentStatus(AppointmentStatus.PAGO);
            appointment.setUpdatedAt(Instant.now());

            appointment = appointmentRepository.save(appointment);

            var payment = Payment.builder()
                    .id(appointment.getId())
                    .appointment(appointment)
                    .transactionId(response.getId())
                    .method(request.paymentMethod())
                    .build();

            return paymentRepository.save(payment);

        } catch (MPApiException mpApiException) {
            throw new APIException(HttpStatus.valueOf(mpApiException.getStatusCode()), mpApiException.getMessage());

        } catch (MPException mpException) {
            throw new APIException(HttpStatus.UNPROCESSABLE_ENTITY, mpException.getMessage());

        }
    }

    public void updatePayment(UUID appointmentId) {
        var appointment = appointmentRepository.findAppointment(appointmentId);

        appointment.setAppointmentStatus(AppointmentStatus.PAGO);
        appointment.setUpdatedAt(Instant.now());

        appointmentRepository.save(appointment);

        var payment = Payment.builder().id(appointmentId).appointment(appointment).method(PaymentMethod.CREDITO).build();

        paymentRepository.save(payment);

    }

    public List<PaymentResponse> getPayments() {
        return paymentRepository.findAll().stream().map(PaymentResponse::new).toList();
    }

    public MercadoPagoTokenResponse getAccessToken() {
        return new MercadoPagoTokenResponse(accessToken);
    }
}
