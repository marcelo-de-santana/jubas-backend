package com.jubasbackend.core.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.appointment.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Appointments")
@RequestMapping("/appointments")
public interface AppointmentApi {

    @Operation(summary = "Buscar agendamentos.",
            description = """
                    Se a requisição for:
                    - Sem parâmetro ou apenas data, será retornado:
                        - Todos dos horários e funcionários disponíveis
                    - Com especialidade:
                        - Apenas os horários e funcionários disponíveis para a especialidade.""",
            responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamentos.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ScheduleResponse>> findAppointments(
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) UUID specialtyId,
            @RequestParam(required = false, defaultValue = "false") boolean filtered
    );

    @Operation(summary = "Buscar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamento.", content = @Content)
    })
    @GetMapping("/{appointmentId}")
    ResponseEntity<AppointmentResponse> findAppointment(@PathVariable UUID appointmentId);

    @Operation(summary = "Buscar dias de atendimento.",
            responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar dias de atendimento.", content = @Content)
    })
    @GetMapping("/daysOfAttendance")
    ResponseEntity<List<DaysOfAttendanceResponse>> findDaysOfAttendance(@RequestParam Optional<LocalDate> startDate,
                                                                        @RequestParam Optional<LocalDate> endDate);

    @Operation(summary = "Cadastrar novo agendamento.", responses = {
            @ApiResponse(responseCode = "201", description = "Agendamento realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Especialidade já agendada para o cliente."),
            @ApiResponse(responseCode = "404", description = "Cliente, funcionário e/ou especialidade não cadastrada."),
            @ApiResponse(responseCode = "409", description = "Conflito de horário."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar o agendamento.")
    })
    @PostMapping
    ResponseEntity<Void> createAppointment(@Valid @RequestBody AppointmentCreateRequest request);

    @Operation(summary = "Bloquear dias para da agenda.", responses = {
            @ApiResponse(responseCode = "201", description = "Dias registrados com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao registrar os dias.")
    })
    @PostMapping("/daysWithoutAttendance")
    ResponseEntity<Void> registerDaysWithoutAttendance(@RequestBody @JsonFormat(pattern = "yyyy-MM-dd") List<LocalDate> dates);

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
    @PutMapping("/rangeOfAttendanceDays")
    ResponseEntity<Void> updateRangeOfAttendanceDays(@RequestBody RangeOfAttendanceRequest request);

    @Operation(summary = "Alterar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Alteração realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao alterar o agendamento.")
    })
    @PatchMapping("/{appointmentId}")
    ResponseEntity<Void> updateAppointment(@PathVariable UUID appointmentId, @RequestBody AppointmentUpdateRequest request);

    @Operation(summary = "Cancelar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Cancelamento realizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao cancelar agendamento.")
    })
    @DeleteMapping("/{appointmentId}/cancelAttendance")
    ResponseEntity<Void> cancelAppointment(@PathVariable UUID appointmentId);

    @Operation(summary = "Desbloquear dias da agenda.", responses = {
            @ApiResponse(responseCode = "204", description = "Dias liberados com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao liberar os dias.")
    })
    @DeleteMapping("/daysWithoutAttendance")
    ResponseEntity<Void> deleteDaysWithoutAttendance(@RequestBody List<LocalDate> dates);
}
