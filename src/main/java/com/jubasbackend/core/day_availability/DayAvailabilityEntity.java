package com.jubasbackend.core.day_availability;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Marcelo Santana
 * Entidade que representa a quantidade de dias em que o estabelecimento disponibilizará a agenda.
 * Exemplo:
 *      quantidade = 0 - Um dia (Hoje)
 *      quantidade = 1 - Dois dias (Hoje e amanhã).
 * Por padrão, a quantidade é igual a zero.
 * Não deve haver mais de um registro na tabela.
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_day_availability")
public class DayAvailabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 1L;
    @Setter
    private int quantity = 0;

}
