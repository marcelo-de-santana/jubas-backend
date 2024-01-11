package com.jubasbackend.service;

import com.jubasbackend.api.dto.response.PermissionResponse;
import com.jubasbackend.api.dto.response.PermissionUserResponse;

import java.util.List;

public interface PermissionService {
    PermissionUserResponse findUsersByPermissionId(Short permissionId);

    List<PermissionResponse> findPermissions();

    List<PermissionUserResponse> findPermissionsAndUsers();
}
