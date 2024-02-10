package com.jubasbackend.core.workingHour;

import com.jubasbackend.core.workingHour.dto.WorkingHourRequest;
import com.jubasbackend.core.workingHour.dto.WorkingHourResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WorkingHourController implements WorkingHourApi {

    private final WorkingHourService service;

    @Override
    public ResponseEntity<List<WorkingHourResponse>> findWorkingHours() {
        return ResponseEntity.ok(service.findWorkingHours());
    }

    @Override
    public ResponseEntity<Void> createWorkingHour(WorkingHourRequest request) {
        var createdWorkingHour = service.createWorkingHour(request);
        return ResponseEntity.created(URI.create("working-hours/" + createdWorkingHour.id())).build();
    }

    @Override
    public ResponseEntity<Void> updateWorkingHour(UUID workingHourId, WorkingHourRequest request) {
        service.updateWorkingHour(workingHourId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteWorkingHour(UUID workingHourId) {
        service.deleteWorkingHour(workingHourId);
        return ResponseEntity.noContent().build();
    }
}
