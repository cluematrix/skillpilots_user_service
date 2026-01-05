package com.skilluser.user.service;

import com.skilluser.user.dto.ModulePermissionBulkDTO;
import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.dto.ModulePermissionGet;
import com.skilluser.user.dto.PermissionRequest;
import com.skilluser.user.model.Module;
import com.skilluser.user.model.ModulePermission;

import java.util.List;
import java.util.Map;

public interface ModuleService {
    Module createModule(Module module);

    ModulePermission findByName(String name);
    List<ModulePermissionDTO> addModulePermissions(ModulePermissionBulkDTO request);
    List<ModulePermission> saveOrUpdatePermissions(Long roleId,
                                                   Long collegeId,
                                                   Long companyId,
                                                   List<ModulePermissionDTO> permissionDTOs);
    List<ModulePermissionGet> getModulePermission(Long userId);

    List<ModulePermission> getPermissionsForRole(Long roleId, Long collegeId, Long companyId);

    void setPermissions(Long roleId, Long collegeId, Long companyId, List<PermissionRequest> permissions) ;

    Map<String, Object> getPermissionsForUser(Long userId) ;
    }
