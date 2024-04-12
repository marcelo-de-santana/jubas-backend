package com.jubasbackend.utils;

import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.domain.repository.NonServiceDayRepository;

import java.time.LocalDate;
import java.util.List;

public class DaysOfAttendanceUtils {

    /*
     * Verifica se há atendimento no dia
     */
    public static boolean isServiceAvailableOnDay(List<LocalDate> nonServiceDays, LocalDate evaluatedDay) {
        return (nonServiceDays.stream()
                .allMatch(dayWithoutService -> dayWithoutService.getDayOfYear() != evaluatedDay.getDayOfYear()));
    }

    /*
     * Adiciona o dia atual ao serviço se estiver disponível ou encontra o próximo disponível
     */
    public static void addAvailableDayOrFindNext(List<ScheduleResponse> serviceDays,
                                                 List<LocalDate> nonServiceDays,
                                                 LocalDate startDate,
                                                 NonServiceDayRepository nonServiceDayRepository) {
        if (isServiceAvailableOnDay(nonServiceDays, startDate)) {
            serviceDays.add(ScheduleResponse.available(startDate));
        } else {
            serviceDays.add(ScheduleResponse.notAvailable(startDate));
            findNextAvailableDay(serviceDays, startDate, nonServiceDayRepository);
        }
    }

    /*
     * Adiciona o próximo dia disponível dentro da semana
     */
    public static void findNextAvailableDay(List<ScheduleResponse> serviceDays,
                                            LocalDate startDate,
                                            NonServiceDayRepository nonServiceDayRepository) {
        // DEFINE O PERÍODO DE BUSCA COMO UMA SEMANA A PARTIR DA DATA DE INÍCIO
        var periodPlusOneWeek = startDate.plusDays(6L);

        // BUSCA OS DIAS INDISPONÍVEIS DENTRO DA SEMANA
        var unavailableDaysOfTheWeek = nonServiceDayRepository.findDateBetween(startDate, periodPlusOneWeek);

        // ITERA PELOS DIAS DENTRO DA SEMANA
        for (int i = 1; startDate.plusDays(i).isBefore(periodPlusOneWeek); i++) {
            var evaluatedDay = startDate.plusDays(i);

            // SE O DIA AVALIADO ESTIVER DISPONÍVEL, ADICIONA AO SERVIÇO E ENCERRA O LOOP
            if (isServiceAvailableOnDay(unavailableDaysOfTheWeek, evaluatedDay)) {
                serviceDays.add(ScheduleResponse.available(evaluatedDay));
                break;
            }

            // SE O DIA NÃO ESTIVER DISPONÍVEL, ADICIONA AO SERVIÇO COM MARCAÇÃO DE INDISPONIBILIDADE
            serviceDays.add(ScheduleResponse.notAvailable(evaluatedDay));
        }
    }

    /*
     * Gera os dias para o período especificado
     */
    public static void generateDaysForThePeriod(List<ScheduleResponse> serviceDays,
                                                List<LocalDate> nonServiceDays,
                                                LocalDate startDate,
                                                LocalDate endDate) {
        for (int i = 1; !startDate.plusDays(i).isAfter(endDate); i++) {
            addServiceDays(serviceDays, nonServiceDays, startDate.plusDays(i));
        }
    }

    /*
     * Adiciona o dia ao serviço com marcação de disponibilidade ou indisponibilidade
     */
    public static void addServiceDays(List<ScheduleResponse> serviceDays,
                                      List<LocalDate> nonServiceDays,
                                      LocalDate evaluatedDay) {
        if (isServiceAvailableOnDay(nonServiceDays, evaluatedDay))
            serviceDays.add(ScheduleResponse.available(evaluatedDay));
        else
            serviceDays.add(ScheduleResponse.notAvailable(evaluatedDay));
    }

}
