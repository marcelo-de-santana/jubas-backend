package com.jubasbackend.service.specialty;

import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.domain.repository.SpecialtyRepository;
import com.jubasbackend.service.SpecialtyService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractSpecialtyServiceTest {

    @Mock
    protected SpecialtyRepository repository;

    @InjectMocks
    protected SpecialtyService service;

    @Captor
    protected ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    protected ArgumentCaptor<Specialty> specialtyEntityArgumentCaptor;
}
