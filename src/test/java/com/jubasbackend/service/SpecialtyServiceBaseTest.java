package com.jubasbackend.service;

import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import com.jubasbackend.infrastructure.repository.SpecialtyRepository;
import com.jubasbackend.service.impl.SpecialtyServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class SpecialtyServiceBaseTest {

    @Mock
    protected SpecialtyRepository repository;

    @InjectMocks
    protected SpecialtyServiceImpl service;

    @Captor
    protected ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    protected ArgumentCaptor<SpecialtyEntity> specialtyEntityArgumentCaptor;
}
