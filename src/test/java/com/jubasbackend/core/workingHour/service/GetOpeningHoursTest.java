package com.jubasbackend.core.workingHour.service;

import com.jubasbackend.core.workingHour.WorkingHourEntityTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.time.LocalTime.parse;
import static org.junit.jupiter.api.Assertions.*;

class GetOpeningHoursTest {

    @Test
    @DisplayName("Deve retornar o primeiro horário igual ao horário de início de jornada e o último igual com 10 minutos de antecedência.")
    void shouldReturnFirstScheduleStartingAtShiftStartAndLastScheduleEndingWith10MinutesEarly() {
        //ARRANGE
        var workingHour = WorkingHourEntityTest.createValidEntity();

        //ACT
        var availableTimes = workingHour.getOpeningHours();

        //ASSERT
        assertNotNull(availableTimes);

        var firstTime = availableTimes.get(0).time();
        var lastTime = availableTimes.get(availableTimes.size() - 1).time();

        assertEquals(parse("07:00"), firstTime);
        assertEquals(parse("15:50"), lastTime);
    }

    @Test
    @DisplayName("Deve marcar como indisponíveis os horários que estiverem dentro do intervalo.")
    void shouldMarkUnavailableForTimesWithinInterval() {
        //ARRANGE
        var workingHour = WorkingHourEntityTest.createValidEntity();

        //ACT
        var availableTimes = workingHour.getOpeningHours();

        //ASSERT
        for (var availableTime : availableTimes) {
            if (workingHour.isInterval(availableTime.time()))
                assertFalse(availableTime.available());
            else
                assertTrue(availableTime.available());
        }
    }
}
