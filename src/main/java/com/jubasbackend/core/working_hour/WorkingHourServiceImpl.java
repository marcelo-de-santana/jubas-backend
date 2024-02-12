package com.jubasbackend.core.working_hour;

import com.jubasbackend.core.working_hour.dto.WorkingHourResponse;
import com.jubasbackend.core.working_hour.dto.WorkingHourRequest;
import com.jubasbackend.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkingHourServiceImpl implements WorkingHourService {

    private final WorkingHourRepository repository;

    @Override
    public List<WorkingHourResponse> findWorkingHours() {
        return repository.findAll().stream().map(WorkingHourResponse::new).toList();
    }

    @Override
    public WorkingHourResponse createWorkingHour(WorkingHourRequest request) {
        var newWorkingHour = new WorkingHourEntity(request);

        if (areTimesRegisteredOnRepository(newWorkingHour))
            throw new ConflictException("Working hours already exists.");

        return new WorkingHourResponse(repository.save(newWorkingHour));
    }

    @Override
    public void updateWorkingHour(UUID workingHourId, WorkingHourRequest request) {
        var workingHour = findWorkingHourOnRepository(workingHourId);
        workingHour.update(request);
        repository.save(workingHour);
    }

    @Override
    public void deleteWorkingHour(UUID workingHourId) {
        repository.deleteById(workingHourId);
    }

    private WorkingHourEntity findWorkingHourOnRepository(UUID workingHourId) {
        return repository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    private boolean areTimesRegisteredOnRepository(WorkingHourEntity entity) {
        return repository.existsByStartTimeAndEndTimeAndStartIntervalAndEndInterval(
                entity.getStartTime(), entity.getEndTime(), entity.getStartInterval(), entity.getEndInterval());
    }
}
