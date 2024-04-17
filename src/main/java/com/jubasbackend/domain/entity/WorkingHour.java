package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.request.WorkingHourRequest;
import com.jubasbackend.controller.response.ScheduleTimeResponse;
import com.jubasbackend.controller.response.ScheduleTimeResponse.WithAppointmentId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"startTime", "endTime", "startInterval", "endInterval"})
@Builder
@Entity(name = "tb_working_hour")
public class WorkingHour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private LocalTime startInterval;

    @NotNull
    private LocalTime endInterval;

    @OneToMany(mappedBy = "workingHour", cascade = CascadeType.ALL)
    private List<Employee> employees;

    public WorkingHour(WorkingHourRequest workingHour) {
        this.startTime = workingHour.startTime();
        this.startInterval = workingHour.startInterval();
        this.endInterval = workingHour.endInterval();
        this.endTime = workingHour.endTime();
        validateEntity();
    }

    public WorkingHour(LocalTime startTime, LocalTime endTime, LocalTime startInterval, LocalTime endInterval) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startInterval = startInterval;
        this.endInterval = endInterval;
        validateEntity();
    }

    public void update(WorkingHourRequest request) {
        if (request.startTime() != null)
            this.startTime = request.startTime();

        if (request.startInterval() != null)
            this.startInterval = request.startInterval();

        if (request.endInterval() != null)
            this.endInterval = request.endInterval();

        if (request.endTime() != null)
            this.endTime = request.endTime();

        validateEntity();
    }

    /**
     * Retorna todos os horários do funcionário, marcando com true para os disponíveis e false para os indisponíveis.
     *
     * @return Lita de Horários com horários e disponibilidade
     */
    public List<ScheduleTimeResponse> getOpeningHours() {
        final int TIME_INTERVAL = 10;
        var openingHours = new ArrayList<ScheduleTimeResponse>();
        var evaluetedTime = startTime;

        openingHours.add(new ScheduleTimeResponse(startTime, true));

        while (evaluetedTime.isBefore(endTime.minusMinutes(TIME_INTERVAL))) {
            var nextTime = evaluetedTime.plusMinutes(TIME_INTERVAL);
            var isAvailable = !isInterval(nextTime);
            openingHours.add(new ScheduleTimeResponse(nextTime, isAvailable));
            evaluetedTime = nextTime;
        }
        return openingHours;
    }

    /**
     * Retornar a agenda do funcionário com os IDs dos agendamentos
     *
     * @return List<ScheduleTimeResponse>
     */
    public List<ScheduleTimeResponse> getAvailableTimes(List<Appointment> appointments) {
        var availableTimes = new ArrayList<ScheduleTimeResponse>();

        for (var openingHour : getOpeningHours()) {
            var appointmentFound = appointments.stream()
                    .filter(appointment -> appointment.isInThePeriod(openingHour.getTime()))
                    .findFirst();

            if (appointmentFound.isPresent())
                availableTimes.add(new WithAppointmentId(openingHour, appointmentFound.get().getId()));
            else
                availableTimes.add(new ScheduleTimeResponse(openingHour));
        }

        return availableTimes;
    }

    /**
     * Retorna os horários possíveis para o funcionário atender determinada especialidade.
     *
     * @return List<ScheduleTimeResponse>
     */
    public List<ScheduleTimeResponse> getPossibleTimes(Specialty specialty, List<Appointment> appointments) {
        var timesToFilter = appointments.isEmpty() ? getOpeningHours() : getAvailableTimes(appointments);
        return filterTimes(specialty, timesToFilter);
    }

    private boolean isInterval(LocalTime time) {
        return (time.equals(this.startInterval) || (time.isAfter(this.startInterval) && time.isBefore(this.endInterval)));
    }

    private void validateEntity() {
        if (this.startTime.isAfter(this.endTime))
            throw new IllegalArgumentException("The start time of the working day cannot be less than the end time.");

        if (this.startTime.isAfter(this.startInterval))
            throw new IllegalArgumentException("The start time cannot be less than the break time.");

        if (this.endTime.isBefore(this.endInterval))
            throw new IllegalArgumentException("The end time cannot be before the break time.");

    }

    /**
     * Filtra os horários possíveis para o funcionário atender a especialidade.
     *
     * @return List<ScheduleTimeResponse>
     */
    private List<ScheduleTimeResponse> filterTimes(Specialty specialty,
                                                   List<ScheduleTimeResponse> timesToFilter) {
        var filteredTimes = new ArrayList<ScheduleTimeResponse>();
        var timeDuration = specialty.getTimeDuration();

        var differenceOfMinutes = timeDuration.getMinute() % 10;

        var updatedTimeDuration = timeDuration.plusMinutes(differenceOfMinutes == 0 ? 0 : 10 - differenceOfMinutes);

        timesToFilter.forEach(currentTime -> {
            if (currentTime.isAvailable()) {
                var endOfAttendance = currentTime.getTime()
                        .plusHours(updatedTimeDuration.getHour())
                        .plusMinutes(updatedTimeDuration.getMinute());

                var lastIsAvailable = false;

                for (ScheduleTimeResponse nextTime : timesToFilter) {
                    if (endOfAttendance.equals(nextTime.getTime()) && lastIsAvailable) {
                        filteredTimes.add(new ScheduleTimeResponse(currentTime.getTime(), true));
                        break;
                    }
                    lastIsAvailable = nextTime.isAvailable();
                }
            }
        });
        return filteredTimes;
    }
}
