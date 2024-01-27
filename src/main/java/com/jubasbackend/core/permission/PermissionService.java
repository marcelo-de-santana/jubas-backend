package com.jubasbackend.core.permission;

import com.jubasbackend.core.permission.dto.PermissionProfileResponse;
import com.jubasbackend.core.permission.dto.PermissionResponse;
import com.jubasbackend.core.permission.dto.PermissionUserProfileResponse;
import com.jubasbackend.core.permission.dto.PermissionUserResponse;

import java.util.List;

public interface PermissionService {
    PermissionUserResponse findUsersByPermission(Short permissionId);

    List<PermissionResponse> findPermissions();

    List<PermissionUserProfileResponse> findPermissionsUsersAndProfiles();

    List<PermissionUserResponse> findPermissionsAndUsers();

    PermissionProfileResponse findProfilesByPermission(Short permissionId);

}
