package com.jubasbackend.core.specialty;

import com.jubasbackend.core.specialty.dto.SpecialtyRequest;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;

import java.util.List;
import java.util.UUID;

public interface SpecialtyService {
    List<SpecialtyResponse> findSpecialties();

    SpecialtyResponse createSpecialty(SpecialtyRequest request);

    void updateSpecialty(UUID specialtyId, SpecialtyRequest request);

    void deleteSpecialty(UUID specialtyId);
}
