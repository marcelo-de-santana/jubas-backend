package com.jubasbackend.core.day_available;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Marcelo Santana
 *
 * Entidade que representa a quantidade de dias em que o estabelecimento disponibilizará a agenda.
 *
 * Ex:
 *      quantidade = 0 - Um dia (Hoje)
 *      quantidade = 1 - Dois dias (Hoje e amanhã).
 *
 * Por padrão a quantidade é igual a zero.
 * Não deve haver mais de um registro na tabela.
 */
@NoArgsConstructor
@Getter
@Entity(name = "tb_day_available")
public class DayAvailableEntity {
    @Id
    private short id = 0;
    @Setter
    private int quantity = 0;

    public DayAvailableEntity(int quantity) {
        this.quantity = quantity;
    }

}
