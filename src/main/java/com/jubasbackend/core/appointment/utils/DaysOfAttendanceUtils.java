package com.jubasbackend.core.appointment.utils;

import com.jubasbackend.core.appointment.dto.DaysOfAttendanceResponse;
import com.jubasbackend.core.non_service_day.NonServiceDayRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DaysOfAttendanceUtils {

    private DaysOfAttendanceUtils() {
    }

    //VERIFICA SE HÁ ATENDIMENTO NO DIA ESPECÍFICO
    public static boolean isServiceAvailableOnDay(List<LocalDate> nonServiceDays, LocalDate evaluatedDay) {
        return (nonServiceDays.stream()
                .noneMatch(dayWithoutService -> dayWithoutService.getDayOfYear() == evaluatedDay.getDayOfYear()));
    }

    public static LocalDate applyEndDateLimits(Optional<LocalDate> optionalStartDate,
                                               Optional<LocalDate> optionalEndDate,
                                               LocalDate startOfPeriod,
                                               int intervalOfDaysToAppointments) {
        var endOfPeriod = optionalEndDate.orElse(startOfPeriod.plusDays(intervalOfDaysToAppointments));

        // LIMITE MÍNIMO PARA A DATA DE FIM DO PERÍODO
        if (endOfPeriod.isBefore(startOfPeriod) || optionalStartDate.isPresent() && optionalEndDate.isEmpty()) {
            endOfPeriod = startOfPeriod;
        }
        // LIMITE MÁXIMO PARA A DATA DE FIM DO PERÍODO
        if (endOfPeriod.isAfter(startOfPeriod.plusYears(1L))) {
            endOfPeriod = startOfPeriod.plusYears(1L);
        }
        return endOfPeriod;
    }

    public static void findNextAvailableDay(List<DaysOfAttendanceResponse> serviceDays,
                                            LocalDate startOfPeriod,
                                            NonServiceDayRepository nonServiceDayRepository) {
        var periodPlusOneMonth = startOfPeriod.plusMonths(1L);
        var unavailableDaysOfTheMonth = nonServiceDayRepository.findDateBetween(startOfPeriod, periodPlusOneMonth);
        for (int i = 1; startOfPeriod.plusDays(i).isBefore(periodPlusOneMonth); i++) {
            var evaluatedDay = startOfPeriod.plusDays(i);

            if (isServiceAvailableOnDay(unavailableDaysOfTheMonth, evaluatedDay)) {
                serviceDays.add(DaysOfAttendanceResponse.available(evaluatedDay));
                break;
            } else {
                serviceDays.add(DaysOfAttendanceResponse.notAvailable(evaluatedDay));
            }
        }
    }

    public static void addServiceDays(List<DaysOfAttendanceResponse> serviceDays,
                                      List<LocalDate> nonServiceDays,
                                      LocalDate evaluatedDay) {
        if (isServiceAvailableOnDay(nonServiceDays, evaluatedDay))
            serviceDays.add(DaysOfAttendanceResponse.available(evaluatedDay));
        else
            serviceDays.add(DaysOfAttendanceResponse.notAvailable(evaluatedDay));
    }

    public static void addAvailableServiceDayOrFindNext(List<DaysOfAttendanceResponse> serviceDays,
                                                        List<LocalDate> nonServiceDays,
                                                        Optional<LocalDate> optionalStartDate,
                                                        Optional<LocalDate> optionalEndDate,
                                                        LocalDate startOfPeriod,
                                                        NonServiceDayRepository nonServiceDayRepository) {
        if (isServiceAvailableOnDay(nonServiceDays, startOfPeriod))
            serviceDays.add(DaysOfAttendanceResponse.available(startOfPeriod));
        else {
            serviceDays.add(DaysOfAttendanceResponse.notAvailable(startOfPeriod));

            if (optionalStartDate.isEmpty() && optionalEndDate.isEmpty())
                findNextAvailableDay(serviceDays, startOfPeriod, nonServiceDayRepository);
        }
    }

    public static void generateDaysForThePeriod(List<DaysOfAttendanceResponse> serviceDays,
                                                List<LocalDate> nonServiceDays,
                                                LocalDate startOfPeriod,
                                                LocalDate endOfPeriod) {
        for (int i = 1; startOfPeriod.plusDays(i).isBefore(endOfPeriod); i++) {
            addServiceDays(serviceDays, nonServiceDays, startOfPeriod.plusDays(i));
        }
    }

    public static void generateDaysAccordingToTheRangeOfDays(List<DaysOfAttendanceResponse> serviceDays,
                                                             List<LocalDate> nonServiceDays,
                                                             LocalDate startOfPeriod,
                                                             int rangeOfDaysForAppointments) {
        for (int i = 1; rangeOfDaysForAppointments > 0 && i <= rangeOfDaysForAppointments; i++) {
            var evaluatedDay = startOfPeriod.plusDays(i);
            if (isServiceAvailableOnDay(nonServiceDays, evaluatedDay))
                serviceDays.add(DaysOfAttendanceResponse.available(evaluatedDay));
        }
    }


}
