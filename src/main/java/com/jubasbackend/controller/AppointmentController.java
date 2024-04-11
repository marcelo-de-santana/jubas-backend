package com.jubasbackend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.controller.response.AppointmentResponse;
import com.jubasbackend.controller.response.EmployeeScheduleResponse;
import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.domain.entity.DayAvailability;
import com.jubasbackend.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Appointments")
@RequiredArgsConstructor
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    @Operation(summary = "Buscar agendamentos.",
            description = "Retorna os agendamentos do dia, os marcados contém os Ids de referência.",
            responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamentos.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EmployeeScheduleResponse>> findAppointments(
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        return ResponseEntity.ok(service.findAppointments(date));
    }

    @Operation(summary = "Buscar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamento.", content = @Content)
    })
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> findAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(service.findAppointment(appointmentId));
    }

    @Operation(summary = "Buscar agenda.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agenda", content = @Content)
    })
    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleResponse>> findSchedule(@RequestParam(required = false) UUID specialtyId) {
        return ResponseEntity.ok(service.findSchedules(specialtyId));
    }

    @Operation(summary = "Buscar dias de atendimento.",
            responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar dias de atendimento.", content = @Content)
    })
    @GetMapping("/days-of-attendance")
    public ResponseEntity<List<ScheduleResponse>> findDaysOfAttendance(@RequestParam(required = false) LocalDate startDate,
                                                                       @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.ok(service.findDaysOfAttendance(startDate, endDate));
    }

    @Operation(summary = "Busca a quantidade de dias disponíveis para agendamento.",
            description = """
                    Se a quantidade for igual a:
                    - Zero: A agenda está disponível em um dia (Hoje).
                    - Um: A agenda está disponível em dois dias dois dias (Hoje e amanhã).
                    - E assim sucessivamente.""",
            responses = {
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar a disponibilidade da agenda.")
            })
    @GetMapping("/range-of-attendance-days")
    public ResponseEntity<DayAvailability> getRangeOfAttendanceDays() {
        return ResponseEntity.ok(service.getRangeOfAttendanceDays());
    }



    @Operation(summary = "Cadastrar novo agendamento.", responses = {
            @ApiResponse(responseCode = "201", description = "Agendamento realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Especialidade já agendada para o cliente."),
            @ApiResponse(responseCode = "404", description = "Cliente, funcionário e/ou especialidade não cadastrada."),
            @ApiResponse(responseCode = "409", description = "Conflito de horário."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar o agendamento.")
    })

    @PostMapping
    public ResponseEntity<Void> createAppointment(@NonNull @RequestBody AppointmentRequest request, JwtAuthenticationToken jwt) {
        var createdAppointment = service.createAppointment(request);
        return ResponseEntity.created(URI.create("/appointments/" + createdAppointment.getId())).build();
    }

    @Operation(summary = "Bloquear dias para da agenda.", responses = {
            @ApiResponse(responseCode = "201", description = "Dias registrados com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao registrar os dias.")
    })
    @PostMapping("/days-without-attendance")
    public ResponseEntity<Void> registerDaysWithoutAttendance(@RequestBody @JsonFormat(pattern = "yyyy-MM-dd") List<LocalDate> dates) {
        service.registerDaysWithoutAttendance(dates);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Alterar o intervalo de disponibilidade da agenda.",
            description = """
                    Se a quantidade for igual a:
                    - Zero: Retorna um dia (Hoje).
                    - Um: Retorna dois dias (Hoje e amanhã).
                    - E assim sucessivamente.""",
            responses = {
            @ApiResponse(responseCode = "204", description = "Alteração realizada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Valor incorreto."),
                    @ApiResponse(responseCode = "500", description = "Erro ao alterar a disponibilidade da agenda.")
    })
    @PutMapping("/range-of-attendance-days/{intervalOfDays}")
    public ResponseEntity<Void> updateRangeOfAttendanceDays(@PathVariable int intervalOfDays) {
        service.updateRangeOfAttendanceDays(intervalOfDays);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Alterar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Alteração realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao alterar o agendamento.")
    })
    @PatchMapping("/{appointmentId}")
    public ResponseEntity<Void> updateAppointment(
            @PathVariable UUID appointmentId,
            @Schema(hidden = true, name = "cliend_id") @RequestBody AppointmentRequest request) {
        service.updateAppointment(appointmentId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancelar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Cancelamento realizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao cancelar agendamento.")
    })
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID appointmentId, JwtAuthenticationToken jwt) {
        service.cancelAppointment(appointmentId, jwt);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desbloquear dias da agenda.", responses = {
            @ApiResponse(responseCode = "204", description = "Dias liberados com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao liberar os dias.")
    })
    @DeleteMapping("/days-without-attendance")
    public ResponseEntity<Void> deleteDaysWithoutAttendance(@RequestBody List<LocalDate> dates) {
        service.deleteDaysWithoutAttendance(dates);
        return ResponseEntity.noContent().build();
    }
}
