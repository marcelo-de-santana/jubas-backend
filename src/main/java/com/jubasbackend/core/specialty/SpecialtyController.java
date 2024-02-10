package com.jubasbackend.core.specialty;

import com.jubasbackend.core.specialty.dto.SpecialtyRequest;
import com.jubasbackend.core.specialty.dto.SpecialtyCategoryResponse;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class SpecialtyController implements SpecialtyApi {

    private final SpecialtyService service;

    @Override
    public ResponseEntity<List<SpecialtyResponse>> findSpecialties() {
        return ResponseEntity.ok(service.findSpecialties());
    }

    @Override
    public ResponseEntity<SpecialtyCategoryResponse> createSpecialty(SpecialtyRequest request) {
        var specialtyCreated = service.createSpecialty(request);
        return ResponseEntity.created(URI.create("/specialties/" + specialtyCreated.id())).body(specialtyCreated);
    }

    @Override
    public ResponseEntity<Void> updateSpecialty(UUID specialtyId, SpecialtyRequest request) {
        service.updateSpecialty(specialtyId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteSpecialty(UUID specialtyId) {
        service.deleteSpecialty(specialtyId);
        return ResponseEntity.noContent().build();
    }
}
