package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.response.ScheduleTimeResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity(name = "tb_employee_associated_specialty")
public class EmployeeSpecialty {

    @EmbeddedId
    private EmployeeSpecialtyId id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("specialtyId")
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    public static EmployeeSpecialty create(UUID employeeId, UUID specialtyId) {
        var compoundId = new EmployeeSpecialtyId(employeeId, specialtyId);
        return EmployeeSpecialty.builder()
                .id(compoundId)
                .employee(Employee.builder()
                        .id(employeeId)
                        .build())
                .specialty(Specialty.builder()
                        .id(specialtyId)
                        .build())
                .build();
    }

    /*
    *  Verifica se a especialidade pode ser realizada pelo funcionário no horário fornecido.
    */
    public boolean isAvailableForAttendance(LocalTime startTime,
                                            List<ScheduleTimeResponse> scheduleOfEmployee) {

        var timeDuration = specialty.getTimeDuration();

        var endTime = startTime
                .plusHours(timeDuration.getHour())
                .plusMinutes(timeDuration.getMinute());

        Integer scheduleStartTimeIndex = null;
        Integer scheduleEndTimeIndex = null;

        // ITERA SOBRE OS HORÁRIOS DISPONÍVEIS PARA ENCONTRAR O PERÍODO
        for (int i = 0; i < scheduleOfEmployee.size(); i++) {
            if (scheduleOfEmployee.get(i).time().equals(startTime)) {
                scheduleStartTimeIndex = i;
            }
            if (scheduleOfEmployee.get(i).time().isAfter(endTime)) {
                scheduleEndTimeIndex = i;
                break;
            }
        }

        if (scheduleStartTimeIndex == null && scheduleEndTimeIndex == null) {
            return false;
        }

        // VERIFICA SE OS HORÁRIOS DO PERÍODO ESTÃO DISPONÍVEIS
        for (int i = scheduleStartTimeIndex; i < scheduleEndTimeIndex; i++) {
            if (!scheduleOfEmployee.get(i).isAvailable()) {
                return false;
            }
        }
        return true;
    }
}
