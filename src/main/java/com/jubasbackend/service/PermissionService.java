package com.jubasbackend.service;

import com.jubasbackend.api.dto.response.PermissionProfileResponse;
import com.jubasbackend.api.dto.response.PermissionResponse;
import com.jubasbackend.api.dto.response.PermissionUserResponse;

import java.util.List;

public interface PermissionService {
    PermissionUserResponse findUsersByPermission(Short permissionId);

    List<PermissionResponse> findPermissions();

    List<PermissionUserResponse> findPermissionsAndUsers();

    PermissionProfileResponse findProfilesByPermission(Short permissionId);
}
