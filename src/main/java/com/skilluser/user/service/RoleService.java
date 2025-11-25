package com.skilluser.user.service;

import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.model.CustomRole;
import com.skilluser.user.model.Role;

import java.util.List;

public interface RoleService {

    public Role createRole(Role role);
    public Role findByName(String name);
    public Role updateRole(Long id, Role updatedRole);
    public void deleteRole(Long id);

    public void savePermissions(Role role, List<ModulePermissionDTO> permissions,
                                 boolean isDefault, Long collegeId, Long companyId);
    public CustomRole createCustomRole(CustomRole customRole);

}
