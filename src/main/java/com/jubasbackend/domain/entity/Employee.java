package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.response.ScheduleTimeResponse;
import com.jubasbackend.controller.response.SpecialtyResponse;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.repository.EmployeeSpecialtyRepository;
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
@Builder
@Entity(name = "tb_employee")
public class Employee {

    @Id
    @Column(name = "employee_id")
    private UUID id;

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "working_hour_id")
    private WorkingHour workingHour;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeSpecialty> specialties;

    public boolean makesSpecialty(UUID specialtyId) {
        return getSpecialties().contains(getCompoundEntity(specialtyId));
    }

    public Specialty getSpecialty(UUID specialtyId) {
        var indexOf = getSpecialties().indexOf(getCompoundEntity(specialtyId));
        return getSpecialties().get(indexOf).getSpecialty();
    }

    /*
     * Busca as especialidades que podem ser atendidas no horário fornecido.
     */
    public List<SpecialtyResponse> getAvailableSpecialties(LocalTime startTime, List<Appointment> appointmentsOfDay){
        var scheduleOfEmployee = getAvailableTimes(appointmentsOfDay);

        return getSpecialties().stream()
                .filter(compoundEntity-> compoundEntity.isAvailableForAttendance(startTime, scheduleOfEmployee))
                .map(EmployeeSpecialty::getSpecialty)
                .map(SpecialtyResponse::new)
                .toList();

    }

    public List<ScheduleTimeResponse> getAvailableTimes(List<Appointment> appointmentsOfDay) {
        return workingHour.getAvailableTimes(getAppointmentsOfEmployee(appointmentsOfDay));
    }

    public List<ScheduleTimeResponse> getPossibleTimes(UUID specialtyId, List<Appointment> appointmentsOfDay) {
        return workingHour.getPossibleTimes(getSpecialty(specialtyId), getAppointmentsOfEmployee(appointmentsOfDay));
    }

    /*
     * Separa os atendimentos do funcionário e ignora os que estão cancelados.
     */
    private List<Appointment> getAppointmentsOfEmployee(List<Appointment> appointments){
        return appointments.stream()
                .filter(appointment -> appointment.getEmployee().getId().equals(id)
                        && appointment.getAppointmentStatus() != AppointmentStatus.CANCELADO)
                .toList();
    }

    public void manageSpecialties(List<UUID> specialties,
                                  EmployeeSpecialtyRepository employeeSpecialtyRepository) {

        var specialtiesToAdd = new ArrayList<EmployeeSpecialty>();
        var specialtiesToRemove = new ArrayList<EmployeeSpecialty>();

        specialties.forEach(specialtyId -> {
            var compoundEntity = getCompoundEntity(specialtyId);

            if (makesSpecialty(specialtyId))
                specialtiesToRemove.add(compoundEntity);
            else
                specialtiesToAdd.add(compoundEntity);

        });

        if (!specialtiesToRemove.isEmpty())
            employeeSpecialtyRepository.deleteAll(specialtiesToRemove);

        if (!specialtiesToAdd.isEmpty())
            employeeSpecialtyRepository.saveAll(specialtiesToAdd);

    }

    private EmployeeSpecialty getCompoundEntity(UUID specialtyId) {
        return EmployeeSpecialty.create(getId(), specialtyId);
    }
}
