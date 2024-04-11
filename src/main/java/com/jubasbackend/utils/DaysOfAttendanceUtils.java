package com.jubasbackend.utils;

import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.domain.repository.NonServiceDayRepository;

import java.time.LocalDate;
import java.util.List;

public class DaysOfAttendanceUtils {

    //VERIFICA SE HÁ ATENDIMENTO NO DIA
    public static boolean isServiceAvailableOnDay(List<LocalDate> nonServiceDays, LocalDate evaluatedDay) {
        return (nonServiceDays.stream()
                .noneMatch(dayWithoutService -> dayWithoutService.getDayOfYear() == evaluatedDay.getDayOfYear()));
    }

    public static LocalDate applyEndDateLimits(LocalDate optionalStartDate,
                                               LocalDate optionalEndDate,
                                               LocalDate startOfPeriod,
                                               int intervalOfDaysToAppointments) {
        var endOfPeriod = optionalEndDate != null ? optionalEndDate : startOfPeriod.plusDays(intervalOfDaysToAppointments);

        // LIMITE MÍNIMO PARA A DATA DE FIM DO PERÍODO
        if (endOfPeriod.isBefore(startOfPeriod) || optionalStartDate != null && optionalEndDate == null) {
            endOfPeriod = startOfPeriod;
        }
        // LIMITE MÁXIMO PARA A DATA DE FIM DO PERÍODO
        if (endOfPeriod.isAfter(startOfPeriod.plusYears(1L))) {
            endOfPeriod = startOfPeriod.plusYears(1L);
        }
        return endOfPeriod;
    }

    public static void findNextAvailableDay(List<ScheduleResponse> serviceDays,
                                            LocalDate startOfPeriod,
                                            NonServiceDayRepository nonServiceDayRepository) {
        var periodPlusOneMonth = startOfPeriod.plusMonths(1L);
        var unavailableDaysOfTheMonth = nonServiceDayRepository.findDateBetween(startOfPeriod, periodPlusOneMonth);
        for (int i = 1; startOfPeriod.plusDays(i).isBefore(periodPlusOneMonth); i++) {
            var evaluatedDay = startOfPeriod.plusDays(i);

            if (isServiceAvailableOnDay(unavailableDaysOfTheMonth, evaluatedDay)) {
                serviceDays.add(ScheduleResponse.available(evaluatedDay));
                break;
            } else {
                serviceDays.add(ScheduleResponse.notAvailable(evaluatedDay));
            }
        }
    }

    public static void addServiceDays(List<ScheduleResponse> serviceDays,
                                      List<LocalDate> nonServiceDays,
                                      LocalDate evaluatedDay) {
        if (isServiceAvailableOnDay(nonServiceDays, evaluatedDay))
            serviceDays.add(ScheduleResponse.available(evaluatedDay));
        else
            serviceDays.add(ScheduleResponse.notAvailable(evaluatedDay));
    }

    public static void addAvailableServiceDayOrFindNext(List<ScheduleResponse> serviceDays,
                                                        List<LocalDate> nonServiceDays,
                                                        LocalDate optionalStartDate,
                                                        LocalDate optionalEndDate,
                                                        LocalDate startOfPeriod,
                                                        NonServiceDayRepository nonServiceDayRepository) {
        if (isServiceAvailableOnDay(nonServiceDays, startOfPeriod))
            serviceDays.add(ScheduleResponse.available(startOfPeriod));
        else {
            serviceDays.add(ScheduleResponse.notAvailable(startOfPeriod));

            if (optionalStartDate == null && optionalEndDate == null)
                findNextAvailableDay(serviceDays, startOfPeriod, nonServiceDayRepository);
        }
    }

    public static void generateDaysForThePeriod(List<ScheduleResponse> serviceDays,
                                                List<LocalDate> nonServiceDays,
                                                LocalDate startOfPeriod,
                                                LocalDate endOfPeriod) {
        for (int i = 1; startOfPeriod.plusDays(i).isBefore(endOfPeriod); i++) {
            addServiceDays(serviceDays, nonServiceDays, startOfPeriod.plusDays(i));
        }
    }

    public static void generateDaysAccordingToTheRangeOfDays(List<ScheduleResponse> serviceDays,
                                                             List<LocalDate> nonServiceDays,
                                                             int rangeOfDaysForAppointments) {
        var lastAssignedDay = serviceDays.get(serviceDays.size() - 1).getDate();

        for (int i = 1; rangeOfDaysForAppointments > 0 && i <= rangeOfDaysForAppointments; i++) {
            var evaluatedDay = lastAssignedDay.plusDays(i);
            if (isServiceAvailableOnDay(nonServiceDays, evaluatedDay))
                serviceDays.add(ScheduleResponse.available(evaluatedDay));
        }
    }


}
