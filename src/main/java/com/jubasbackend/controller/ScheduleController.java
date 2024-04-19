package com.jubasbackend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.controller.response.EmployeeScheduleTimeResponse;
import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.domain.entity.DayAvailability;
import com.jubasbackend.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Schedules")
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService service;

    @Operation(summary = "Buscar agenda.",
            description = "Recurso destinado aos clientes, pois retorna apenas os dias e horários disponíveis.",
            responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agenda", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedule(@RequestParam(required = false) UUID specialtyId) {
        return ResponseEntity.ok(service.getSchedules(specialtyId));
    }

    @Operation(summary = "Buscar agenda do dia.",
            description = "Retorna os agendamentos do dia, os marcados contém os Ids de referência.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamentos.", content = @Content)
    })
    @GetMapping("/by-date")
    public ResponseEntity<List<EmployeeScheduleTimeResponse>> getSchedule(
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        return ResponseEntity.ok(service.getSchedule(date));
    }

    @Operation(summary = "Buscar dias de atendimento.",
            description = """
                    Retorna o dia atual e os próximos da semana, indicando sua disponibilidade.
                    - O corpo da resposta inclui o dia e um booleano, onde true representa disponibilidade
                    e false indisponibilidade.
                    - Se nenhum parâmetro for fornecido, a busca considera o intervalo de agenda definido.                    
                    - Se o dia atual estiver bloqueado, o sistema tentará encontrar o próximo dia disponível.                    
                    - Se a agenda estiver bloqueada por muitos dias, a busca pelo próximo dia disponível é limitada
                    a uma semana.                    
                    - Se os parâmetros forem fornecidos, as datas serão usadas como base para a busca.
                    """,

            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar dias de atendimento.", content = @Content)
            })
    @GetMapping("/days-of-attendance")
    public ResponseEntity<List<ScheduleResponse>> getDaysOfAttendance(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return ResponseEntity.ok(service.getDaysOfAttendance(startDate, endDate));
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

    @Operation(summary = "Bloquear dias para da agenda.", responses = {
            @ApiResponse(responseCode = "201", description = "Dias registrados com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao registrar os dias.")
    })
    @PostMapping("/days-without-attendance")
    public ResponseEntity<Void> registerDaysWithoutAttendance(
            @RequestBody @JsonFormat(pattern = "yyyy-MM-dd") List<LocalDate> dates) {
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
