package com.jubasbackend.core.working_hour;

import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse.WithId;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse.WithoutId;
import com.jubasbackend.core.working_hour.dto.WorkingHourRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"startTime", "endTime", "startInterval", "endInterval"})
@Builder
@Entity(name = "tb_working_hour")
public class WorkingHourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime startInterval;
    private LocalTime endInterval;

    @OneToMany(mappedBy = "workingHour", cascade = CascadeType.ALL)
    private List<EmployeeEntity> employees;

    public WorkingHourEntity(WorkingHourRequest workingHour) {
        this.startTime = workingHour.startTime();
        this.startInterval = workingHour.startInterval();
        this.endInterval = workingHour.endInterval();
        this.endTime = workingHour.endTime();
        validateEntity();
    }

    public WorkingHourEntity(LocalTime startTime, LocalTime endTime, LocalTime startInterval, LocalTime endInterval) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startInterval = startInterval;
        this.endInterval = endInterval;
        validateEntity();
    }

    public void update(WorkingHourRequest request) {
        this.startTime = request.startTime();
        this.endTime = request.endTime();
        this.startInterval = request.startInterval();
        this.endInterval = request.endInterval();
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

        openingHours.add(new WithoutId(startTime, true));

        while (evaluetedTime.isBefore(endTime.minusMinutes(TIME_INTERVAL))) {
            var nextTime = evaluetedTime.plusMinutes(TIME_INTERVAL);
            var isAvailable = !isInterval(nextTime);
            openingHours.add(new WithoutId(nextTime, isAvailable));
            evaluetedTime = nextTime;
        }
        return openingHours;
    }

    /**
     * Retornar a agenda do funcionário com os IDs dos agendamentos
     *
     * @return List<ScheduleTimeResponse>
     */
    public List<ScheduleTimeResponse> getAvailableTimes(List<AppointmentEntity> appointments) {
        var availableTimes = new ArrayList<ScheduleTimeResponse>();

        for (var openingHour : getOpeningHours()) {
            var appointmentFound = appointments.stream()
                    .filter(appointment -> appointment.isInThePeriod(openingHour.time()))
                    .findFirst();

            if (appointmentFound.isPresent())
                availableTimes.add(new WithId(openingHour, appointmentFound.get().getId()));
            else
                availableTimes.add(new WithoutId(openingHour));
        }

        return availableTimes;
    }

    /**
     * Retorna os horários possíveis para o funcionário atender determinada especialidade.
     *
     * @return List<ScheduleTimeResponse>
     */
    public List<ScheduleTimeResponse> getPossibleTimes(SpecialtyEntity specialty, List<AppointmentEntity> appointments) {
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
    private List<ScheduleTimeResponse> filterTimes(SpecialtyEntity specialty,
                                        List<? extends ScheduleTimeResponse> timesToFilter) {
        var filteredOpeningHours = new ArrayList<ScheduleTimeResponse>();
        var specialtyTimeDuration = specialty.getTimeDuration();
        var lastIsAvailable = new AtomicBoolean(false);

        timesToFilter.forEach(firstLoop -> {
            if (firstLoop.isAvailable()) {
                var endOfAttendance = firstLoop.time()
                        .plusHours(specialtyTimeDuration.getHour())
                        .plusMinutes(specialtyTimeDuration.getMinute());

                timesToFilter.forEach(secondLoop -> {
                    if (endOfAttendance.equals(secondLoop.time()) && (secondLoop.isAvailable() || lastIsAvailable.get()))
                        filteredOpeningHours.add(new WithoutId(firstLoop.time(), firstLoop.isAvailable()));

                    lastIsAvailable.set(secondLoop.isAvailable());
                });
            }
        });

        return filteredOpeningHours;
    }
}
