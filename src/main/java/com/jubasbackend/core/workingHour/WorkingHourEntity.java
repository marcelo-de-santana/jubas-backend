package com.jubasbackend.core.workingHour;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.workingHour.dto.AvailableTimeResponse;
import com.jubasbackend.core.workingHour.dto.WorkingHourRequest;
import jakarta.persistence.*;
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

    public boolean isInterval(LocalTime time) {
        return (time.equals(this.startInterval) || (time.isAfter(this.startInterval) && time.isBefore(this.endInterval)));
    }

    public List<AvailableTimeResponse> getOpeningHours() {
        var openingHours = new ArrayList<AvailableTimeResponse>();
        var lastTime = startTime;

        openingHours.add(new AvailableTimeResponse(startTime, true));

        while (lastTime.isBefore(endTime.minusMinutes(10))) {
            lastTime = openingHours.get(openingHours.size() - 1).time();

            var newTime = lastTime.plusMinutes(10);
            if (!newTime.equals(endTime)) {
                var isAvailable = !isInterval(newTime);
                openingHours.add(new AvailableTimeResponse(newTime, isAvailable));
            }
        }
        return openingHours;
    }
}
