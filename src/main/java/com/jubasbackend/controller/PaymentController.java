package com.jubasbackend.controller;

import com.jubasbackend.controller.request.PaymentRequest;
import com.jubasbackend.controller.response.MercadoPagoTokenResponse;
import com.jubasbackend.controller.response.PaymentResponse;
import com.jubasbackend.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Payments")
@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPayments() {
        return ResponseEntity.ok(paymentService.getPayments());
    }

    @GetMapping("/token")
    public ResponseEntity<MercadoPagoTokenResponse> getAccessToken() {
        return ResponseEntity.ok(paymentService.getAccessToken());
    }

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody @Valid PaymentRequest request) {
        var payment = paymentService.createPayment(request);
        return ResponseEntity.created(URI.create("/payments/" + payment.getId())).build();
    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<Void> updatePayment(@PathVariable UUID appointmentId) {
        paymentService.updatePayment(appointmentId);
        return ResponseEntity.noContent().build();
    }

}
