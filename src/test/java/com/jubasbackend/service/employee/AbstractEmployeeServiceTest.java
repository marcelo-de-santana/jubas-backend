package com.jubasbackend.service.employee;

import com.jubasbackend.service.TestEntityFactory;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.controller.request.EmployeeRequest;
import com.jubasbackend.domain.entity.EmployeeSpecialty;
import com.jubasbackend.domain.entity.EmployeeSpecialtyId;
import com.jubasbackend.domain.repository.EmployeeSpecialtyRepository;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.service.EmployeeService;
import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.domain.entity.WorkingHour;
import com.jubasbackend.domain.repository.WorkingHourRepository;
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
public abstract class AbstractEmployeeServiceTest extends TestEntityFactory {

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    ArgumentCaptor<Employee> employeeEntityCaptor;

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
    protected EmployeeService service;

    UUID employeeId = UUID.randomUUID();
    UUID profileId = employeeId;
    UUID workingHourId = UUID.randomUUID();
    UUID specialtyId = UUID.randomUUID();

    Profile profile = Profile.builder().id(profileId).name("José Navalha").statusProfile(true).build();
    Specialty specialty = Specialty.builder().id(specialtyId).name("Corte de cabelo").build();
    WorkingHour workingHour = WorkingHour.builder().id(workingHourId).build();
    EmployeeSpecialty compoundEntity = mockCompoundEntity(profile, specialty);
    Employee savedEmployee = Employee.builder()
            .id(employeeId)
            .profile(profile)
            .workingHour(workingHour)
            .specialties(new ArrayList<>(List.of(compoundEntity))).build();

    EmployeeSpecialty mockCompoundEntity(Profile profile, Specialty specialty) {
        var compoundId = new EmployeeSpecialtyId(profile.getId(), specialty.getId());
        var employeeEntity = Employee.builder().id(profile.getId()).profile(profile).build();
        return new EmployeeSpecialty(compoundId, employeeEntity, specialty);
    }

    EmployeeRequest request = new EmployeeRequest(profileId, workingHourId, List.of(specialtyId));

}
