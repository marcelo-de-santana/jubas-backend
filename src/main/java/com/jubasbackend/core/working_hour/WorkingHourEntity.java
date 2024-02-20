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

    public void validateEntity() {
        if (this.startTime.isAfter(this.endTime))
            throw new IllegalArgumentException("The start time of the working day cannot be less than the end time.");

        if (this.startTime.isAfter(this.startInterval))
            throw new IllegalArgumentException("The start time cannot be less than the break time.");

        if (this.endTime.isBefore(this.endInterval))
            throw new IllegalArgumentException("The end time cannot be before the break time.");

    }

    /**
     * Retorna todos os horários do funcionário, marcando com true para os disponíveis e false para os indisponíveis.
     *
     * @return Lita de Horários com horários e disponibilidade
     */
    public List<WithoutId> getOpeningHours() {
        var openingHours = new ArrayList<WithoutId>();
        var lastTime = startTime;

        openingHours.add(new WithoutId(startTime, true));

        while (lastTime.isBefore(endTime.minusMinutes(10))) {
            lastTime = openingHours.get(openingHours.size() - 1).time();

            var newTime = lastTime.plusMinutes(10);
            if (!newTime.equals(endTime)) {
                var isAvailable = !isInterval(newTime);
                openingHours.add(new WithoutId(newTime, isAvailable));
            }
        }
        return openingHours;
    }

    /**
     * Retornar a agenda do funcionário
     *
     * @param appointments
     * @return ScheduleTimeResponse
     */
    public List<? extends ScheduleTimeResponse> getAvailableTimes(List<AppointmentEntity> appointments) {
        var availableTimes = new ArrayList<ScheduleTimeResponse>();
        //MARCA HORÁRIOS AGENDADOS
        getOpeningHours().forEach(openingHour -> {
            var appointmentMatch = appointments.stream()
                    .filter(appointment -> appointment.isInThePeriod(openingHour.time()))
                    .findFirst();

            if (appointmentMatch.isPresent())
                availableTimes.add(new WithId(openingHour, appointmentMatch.get().getId()));
            else
                availableTimes.add(new WithoutId(openingHour));

        });

        return availableTimes;
    }

    /**
     * Retorna os horários possíveis para o funcionário atender determinada especialidade.
     *
     * @param specialty
     * @param appointments
     * @return
     */
    public List<WithoutId> getPossibleTimes(SpecialtyEntity specialty, List<AppointmentEntity> appointments) {
        if (appointments.isEmpty())
            return filterTimes(specialty, getOpeningHours());

        return filterTimes(specialty, getAvailableTimes(appointments));
    }

    /**
     * Filtra os horários possíveis para o funcionário atender a especialidade.
     *
     * @param specialty
     * @return
     */
    private List<WithoutId> filterTimes(SpecialtyEntity specialty, List<? extends ScheduleTimeResponse> timesToFilter) {
        var filteredOpeningHours = new ArrayList<WithoutId>();
        var timeDuration = specialty.getTimeDuration();

        timesToFilter.forEach(firstLoop -> {
            if (firstLoop.isAvailable()) {
                var endOfAttendance = firstLoop.time()
                        .plusHours(timeDuration.getHour())
                        .plusMinutes(timeDuration.getMinute());

                AtomicBoolean lastIsAvailable = new AtomicBoolean(false);
                timesToFilter.forEach(secondLoop -> {
                    if (endOfAttendance.equals(secondLoop.time()) && (secondLoop.isAvailable() || lastIsAvailable.get()))
                        filteredOpeningHours.add(new WithoutId(firstLoop.time(), firstLoop.isAvailable()));

                    lastIsAvailable.set(secondLoop.isAvailable());
                });
            }
        });

        return filteredOpeningHours;
    }


    //TODO MODIFICAR PARA PRIVADO
    public boolean isInterval(LocalTime time) {
        return (time.equals(this.startInterval) || (time.isAfter(this.startInterval) && time.isBefore(this.endInterval)));
    }
}
