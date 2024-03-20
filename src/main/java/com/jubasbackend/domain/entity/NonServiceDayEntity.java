package com.jubasbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Marcelo Santana
 *
 * Entidade que representa os dias em que o estabelecimento não atenderá (finais de semana, feriados, etc.)
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "tb_non_service_day")
public class NonServiceDayEntity {
    @Id
    @Schema(type = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
