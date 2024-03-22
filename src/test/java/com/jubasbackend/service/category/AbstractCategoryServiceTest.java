package com.jubasbackend.service.category;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.repository.CategoryRepository;
import com.jubasbackend.service.CategoryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractCategoryServiceTest {

    @Mock
    protected CategoryRepository repository;

    @InjectMocks
    protected CategoryService service;

    @Captor
    protected ArgumentCaptor<Short> shortArgumentCaptor;

    @Captor
    protected ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    protected ArgumentCaptor<Category> categoryEntityArgumentCaptor;
}
