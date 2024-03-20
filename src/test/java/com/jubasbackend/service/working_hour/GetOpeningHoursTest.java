package com.jubasbackend.service.working_hour;

import com.jubasbackend.domain.entity.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.UUID;

import static java.time.LocalTime.parse;
import static org.junit.jupiter.api.Assertions.*;

class GetOpeningHoursTest {

    @Test
    @DisplayName("Deve retornar o primeiro horário igual ao horário de início de jornada e o último igual com 10 minutos de antecedência.")
    void shouldReturnFirstScheduleStartingAtShiftStartAndLastScheduleEndingWith10MinutesEarly() {
        //ARRANGE
        var workingHour = WorkingHourEntity.builder()
                .id(UUID.randomUUID())
                .startTime(parse("07:00"))
                .endTime(parse("16:00"))
                .startInterval(parse("11:00"))
                .endInterval(parse("12:00")).build();

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
        var workingHour = WorkingHourEntity.builder()
                .id(UUID.randomUUID())
                .startTime(parse("07:00"))
                .endTime(parse("16:00"))
                .startInterval(parse("11:00"))
                .endInterval(parse("12:00")).build();

        //ACT
        var availableTimes = workingHour.getOpeningHours();

        //ASSERT
        for (var availableTime : availableTimes) {

            if (isInterval(workingHour, availableTime.time()))

                assertFalse(availableTime.isAvailable());
            else
                assertTrue(availableTime.isAvailable());
        }
    }

    private boolean isInterval(WorkingHourEntity workingHour, LocalTime time) {
        return (time.equals(workingHour.getStartInterval()) ||
                (time.isAfter(workingHour.getStartInterval())
                        && time.isBefore(workingHour.getEndInterval())));
    }
}
