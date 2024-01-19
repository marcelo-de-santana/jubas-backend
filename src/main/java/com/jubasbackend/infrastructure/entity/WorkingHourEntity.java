package com.jubasbackend.infrastructure.entity;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    }
}
