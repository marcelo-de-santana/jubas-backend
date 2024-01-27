package com.jubasbackend.core.specialty;

import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.specialty.SpecialtyRepository;
import com.jubasbackend.core.specialty.SpecialtyServiceImpl;
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
