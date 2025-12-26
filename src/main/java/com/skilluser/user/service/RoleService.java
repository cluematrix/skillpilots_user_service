package com.skilluser.user.service;

import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.model.CustomRole;
import com.skilluser.user.model.Role;

import java.util.List;

public interface RoleService {

    Role createRole(Role role);
    Role findByName(String name);
    Role updateRole(Long id, Role updatedRole);
    void deleteRole(Long id);

    void savePermissions(Role role, List<ModulePermissionDTO> permissions,
                         boolean isDefault, Long collegeId, Long companyId);
    CustomRole createCustomRole(CustomRole customRole);

}
