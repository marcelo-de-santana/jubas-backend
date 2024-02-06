package com.jubasbackend.core.permission;

import com.jubasbackend.core.profile.dto.ProfileResponse;
import com.jubasbackend.core.user.dto.UserResponse;
import com.jubasbackend.core.user.enums.PermissionType;

import java.util.List;

public interface PermissionService {

    List<UserResponse> findUsersByPermission(PermissionType permission);

    List<ProfileResponse> findProfilesByPermission(PermissionType permission);
}
