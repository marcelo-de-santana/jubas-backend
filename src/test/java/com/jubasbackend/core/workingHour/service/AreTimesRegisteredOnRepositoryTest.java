package com.jubasbackend.core.workingHour.service;

import com.jubasbackend.core.workingHour.WorkingHourEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.time.LocalTime;

import static java.time.LocalTime.parse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AreTimesRegisteredOnRepositoryTest extends WorkingHourServiceBaseTest {

    @Captor
    protected ArgumentCaptor<LocalTime> timeCaptor;

    @Test
    @DisplayName("Verifica a ordem dos parâmetros passados para o método.")
    void ChecksTheOrderOfParametersPassedToTheMethod() {
        //ARRANGE
        var entity = WorkingHourEntity.builder()
                .startTime(parse("11:00"))
                .endTime(parse("17:00"))
                .startInterval(parse("12:00"))
                .endInterval(parse("13:00")).build();

        doReturn(true).when(repository)
                .existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                        timeCaptor.capture(),
                        timeCaptor.capture(),
                        timeCaptor.capture(),
                        timeCaptor.capture());

        //ACT & ARRANGE
        assertDoesNotThrow(() -> service.areTimesRegisteredOnRepository(entity));

        var capturedTimes = timeCaptor.getAllValues();

        assertEquals(entity.getStartTime(), capturedTimes.get(0));
        assertEquals(entity.getEndTime(), capturedTimes.get(1));
        assertEquals(entity.getStartInterval(), capturedTimes.get(2));
        assertEquals(entity.getEndInterval(), capturedTimes.get(3));

        verify(repository).existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                capturedTimes.get(0),
                capturedTimes.get(1),
                capturedTimes.get(2),
                capturedTimes.get(3));

        verifyNoMoreInteractions(repository);
    }
}
