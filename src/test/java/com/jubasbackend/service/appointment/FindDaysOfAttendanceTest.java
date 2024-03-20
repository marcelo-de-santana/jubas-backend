package com.jubasbackend.service.appointment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class FindDaysOfAttendanceTest extends AbstractAppointmentServiceTest {

    @Test
    @DisplayName("Retornar um dia se o intervalo Zero.")
    void returnOneDayIfZeroInterval() {
        //ARRANGE
        mockDayAvailabilityRepositoryFindQuantity(0);

        //ACT
        var response = service.findDaysOfAttendance(null, null);

        //ASSERT
        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.get(0).isAvailable());
    }

    @Test
    @DisplayName("Retornar dois dias se intervalo um.")
    void returnTwoDaysIfIntervalOne() {
        //ARRANGE
        mockDayAvailabilityRepositoryFindQuantity(1);

        //ACT & ASSERT
        var response = service.findDaysOfAttendance(null, null);
        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("Obter próximo dia disponível se atual bloqueado.")
    void getNextAvailableDayIfCurrentBlocked() {
        //ARRANGE
        var blockedDay = LocalDate.now();

        mockDayAvailabilityRepositoryFindQuantity(0);
        mockNonServiceDayRepositoryFindDateBetween(List.of(blockedDay));

        //ACT
        var response = service.findDaysOfAttendance(null, null);

        //ASSERT
        assertFalse(response.get(0).isAvailable());
        assertEquals(blockedDay, response.get(0).date());

        assertTrue(response.get(1).isAvailable());
        assertEquals(blockedDay.plusDays(1L), response.get(1).date());

    }

    @Test
    @DisplayName("Obter somente dia solicitado se somente data início informada.")
    void RetrieveOnlyRequestedDayIfOnlyStartDateProvided() {
        //ARRANGE
        var currentDay = LocalDate.now();
        mockDayAvailabilityRepositoryFindQuantity(10);

        //ACT & ASSERT
        var response = service.findDaysOfAttendance(currentDay, null);
        assertEquals(1, response.size());
        assertEquals(currentDay, response.get(0).date());
    }

    @Test
    @DisplayName("Deve retornar somente o dia se data final for igual à data atual.")
    void shouldReturnDayIfEndDateEqualsToday() {
        //ARRANGE
        var currentDay = LocalDate.now();
        mockDayAvailabilityRepositoryFindQuantity(2);

        //ACT & ASSERT
        var response = service.findDaysOfAttendance(null, currentDay);
        assertEquals(1, response.size());
        assertEquals(currentDay, response.get(0).date());
    }

    @Test
    @DisplayName("Deve retornar dia atual se data fim passada e anterior.")
    void shouldReturnCurrentDayIfEndDateIsPassedAndBeforeToday() {
        //ARRANGE
        mockDayAvailabilityRepositoryFindQuantity(2);
        var dayBefore = LocalDate.now().minusDays(1L);

        //ACT & ASSERT
        var response = service.findDaysOfAttendance(null, dayBefore);
        assertEquals(1, response.size());
        assertEquals(LocalDate.now(), response.get(0).date());
    }

    @Test
    @DisplayName("Deve retornar dias do período.")
    void shouldReturnDaysInPeriod() {
        //ARRANGE
        mockDayAvailabilityRepositoryFindQuantity(2);
        var today = LocalDate.now();
        var dayPlusOneMonth = today.plusMonths(1L);

        //ACT & ASSERT
        var response = service.findDaysOfAttendance(today, dayPlusOneMonth);
        assertEquals(LocalDate.now(), response.get(0).date());
        assertEquals(dayPlusOneMonth.minusDays(1L), response.get(response.size() - 1).date());
    }

    @Test
    @DisplayName("Calcular dias do ano em período extenso.")
    void calculateAvailableDaysInExtendedPeriod() {
        //ARRANGE
        mockDayAvailabilityRepositoryFindQuantity(0);
        var today = LocalDate.now();
        var dayPlusTwoYear = today.plusYears(2L);

        //ACT & ASSERT
        var response = service.findDaysOfAttendance(today, dayPlusTwoYear);
        assertEquals(LocalDate.now(), response.get(0).date());
        assertEquals(dayPlusTwoYear.minusYears(1L).minusDays(1L), response.get(response.size() - 1).date());
    }

    void mockDayAvailabilityRepositoryFindQuantity(int returnedValue) {
        doReturn(returnedValue).when(dayAvailabilityRepository).findQuantity();
    }

    void mockNonServiceDayRepositoryFindDateBetween(List<LocalDate> blockedDays) {
        doReturn(blockedDays).when(nonServiceDayRepository).findDateBetween(any(), any());
    }

}
