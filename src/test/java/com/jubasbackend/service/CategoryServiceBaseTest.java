package com.jubasbackend.service;

import com.jubasbackend.infrastructure.entity.CategoryEntity;
import com.jubasbackend.infrastructure.repository.CategoryRepository;
import com.jubasbackend.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class CategoryServiceBaseTest {

    @Mock
    protected CategoryRepository repository;

    @InjectMocks
    protected CategoryServiceImpl service;

    @Captor
    protected ArgumentCaptor<Short> shortArgumentCaptor;

    @Captor
    protected ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    protected ArgumentCaptor<CategoryEntity> categoryEntityArgumentCaptor;
}
