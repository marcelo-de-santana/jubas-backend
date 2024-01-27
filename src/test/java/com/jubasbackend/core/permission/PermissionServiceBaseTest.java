package com.jubasbackend.core.permission;

import com.jubasbackend.core.permission.PermissionRepository;
import com.jubasbackend.core.permission.PermissionServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class PermissionServiceBaseTest {
    @Mock
    protected PermissionRepository repository;

    @InjectMocks
    protected PermissionServiceImpl service;
}
