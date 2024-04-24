package com.jubasbackend.controller;

import com.jubasbackend.controller.request.FeedbackRequest;
import com.jubasbackend.controller.response.FeedbackResponse;
import com.jubasbackend.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@Tag(name = "Feedbacks")
@RequiredArgsConstructor
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService service;

    @Operation(summary = "Buscar Feedbacks", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar feedbacks.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getFeedbacks() {
        return ResponseEntity.ok(service.getFeedbacks());
    }

    @Operation(summary = "Buscar Feedback", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Feedback não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar feedback.", content = @Content)
    })
    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackResponse> getFeedback(@PathVariable UUID feedbackId) {
        return ResponseEntity.ok(service.getFeedback(feedbackId));
    }

    @Operation(summary = "Criar ou atualizar feedback", responses = {
            @ApiResponse(responseCode = "201", description = "Feedback registrado/atualizado com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar/atualizar feedbacks.")
    })
    @PutMapping
    public ResponseEntity<Void> createFeedback(@RequestBody FeedbackRequest request) {
        var newFeedback = service.createFeedback(request);
        return ResponseEntity.created(URI.create("/feedbacks" + newFeedback.getId())).build();
    }

}
