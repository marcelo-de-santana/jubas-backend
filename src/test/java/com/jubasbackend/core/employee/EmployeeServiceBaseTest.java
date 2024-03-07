package com.jubasbackend.core.employee;

import com.jubasbackend.core.TestEntityFactory;
import com.jubasbackend.core.appointment.AppointmentRepository;
import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyId;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyRepository;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.profile.ProfileRepository;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import com.jubasbackend.core.working_hour.WorkingHourRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class EmployeeServiceBaseTest extends TestEntityFactory {

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    ArgumentCaptor<EmployeeEntity> employeeEntityCaptor;

    @Captor
    ArgumentCaptor<LocalDateTime> localDateTimeCaptor;

    @Mock
    protected EmployeeRepository employeeRepository;

    @Mock
    protected ProfileRepository profileRepository;

    @Mock
    protected WorkingHourRepository workingHourRepository;

    @Mock
    protected EmployeeSpecialtyRepository employeeSpecialtyRepository;

    @Mock
    protected AppointmentRepository appointmentRepository;

    @InjectMocks
    protected EmployeeServiceImpl service;

    UUID employeeId = UUID.randomUUID();
    UUID profileId = employeeId;
    UUID workingHourId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();

    ProfileEntity profile = ProfileEntity.builder().id(profileId).name("José Navalha").statusProfile(true).build();
    SpecialtyEntity specialty = SpecialtyEntity.builder().id(specialtyId).name("Corte de cabelo").build();
    WorkingHourEntity workingHour = WorkingHourEntity.builder().id(workingHourId).build();
    EmployeeSpecialtyEntity compoundEntity = mockCompoundEntity(profile, specialty);
    EmployeeEntity savedEmployee = EmployeeEntity.builder()
            .id(employeeId)
            .profile(profile)
            .workingHour(workingHour)
            .specialties(new ArrayList<>(List.of(compoundEntity))).build();

    EmployeeSpecialtyEntity mockCompoundEntity(ProfileEntity profile, SpecialtyEntity specialty) {
        var compoundId = new EmployeeSpecialtyId(profile.getId(), specialty.getId());
        var employeeEntity = EmployeeEntity.builder().id(profile.getId()).profile(profile).build();
        return new EmployeeSpecialtyEntity(compoundId, employeeEntity, specialty);
    }

    EmployeeRequest request = new EmployeeRequest(profileId, workingHourId, List.of(specialtyId));

}
