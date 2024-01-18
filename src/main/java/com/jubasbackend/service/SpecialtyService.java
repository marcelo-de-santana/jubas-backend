package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.SpecialtyRequest;
import com.jubasbackend.api.dto.response.SpecialtyCategoryResponse;
import com.jubasbackend.api.dto.response.SpecialtyResponse;

import java.util.List;
import java.util.UUID;

public interface SpecialtyService {
    List<SpecialtyResponse> findSpecialties();

    SpecialtyCategoryResponse createSpecialty(SpecialtyRequest request);

    void updateSpecialty(UUID specialtyId, SpecialtyRequest request);

    void deleteSpecialty(UUID specialtyId);
}
