package com.jubasbackend.core.appointment;

import org.junit.jupiter.api.Test;

public class FindAppointmentsTest extends AppointmentServiceBaseTest {
    //TODO SPECIALTY PARAM TESTS

    @Test
    void deveRetornarTodosOsHorariosDoDia(){
        //O DIA É PASSADO E TODOS OS HORÁRIOS SÃO RETORNADOS
        //service.findAppointments();
    }

    @Test
    void deveRetornarSomenteOsHorariosDisponíveisParaRealizarAEspecialidade(){
        //A ESPECIALIDADE É PASSADA E SOMENTE OS HORÁRIOS QUE SE ENCAIXAM NA ESPECIALIDADE SERÃO RETORNADOS
    }

}
