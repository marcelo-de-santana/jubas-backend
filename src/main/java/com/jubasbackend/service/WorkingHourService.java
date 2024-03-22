package com.jubasbackend.service;

import com.jubasbackend.controller.request.WorkingHourRequest;
import com.jubasbackend.controller.response.WorkingHourResponse;
import com.jubasbackend.domain.entity.WorkingHour;
import com.jubasbackend.domain.repository.WorkingHourRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class WorkingHourService {

    private final WorkingHourRepository repository;

    public List<WorkingHourResponse> findWorkingHours() {
        return repository.findAll().stream().map(WorkingHourResponse::new).toList();
    }

    public WorkingHourResponse createWorkingHour(WorkingHourRequest request) {
        var newWorkingHour = new WorkingHour(request);

        if (areTimesRegistered(newWorkingHour))
            throw new APIException(CONFLICT, "Working hours already exists.");

        return new WorkingHourResponse(repository.save(newWorkingHour));
    }

    public void updateWorkingHour(UUID workingHourId, WorkingHourRequest request) {
        var workingHour = getWorkingHour(workingHourId);
        workingHour.update(request);
        repository.save(workingHour);
    }

    public void deleteWorkingHour(UUID workingHourId) {
        repository.deleteById(workingHourId);
    }

    private WorkingHour getWorkingHour(UUID workingHourId) {
        return repository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    private boolean areTimesRegistered(WorkingHour entity) {
        return repository.areTimesExists(entity.getStartTime(),
                entity.getEndTime(),
                entity.getStartInterval(),
                entity.getEndInterval());
    }
}
