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
    public Module createModule(Module module);

    public ModulePermission findByName(String name);
    public List<ModulePermissionDTO> addModulePermissions(ModulePermissionBulkDTO request);
    public List<ModulePermission> saveOrUpdatePermissions(Long roleId,
                                                          Long collegeId,
                                                          Long companyId,
                                                          List<ModulePermissionDTO> permissionDTOs);
    List<ModulePermissionGet> getModulePermission(Long userId);

    public List<ModulePermission> getPermissionsForRole(Long roleId, Long collegeId, Long companyId);

    public void setPermissions(Long roleId, Long collegeId, Long companyId, List<PermissionRequest> permissions) ;

    public Map<String, Object> getPermissionsForUser(Long userId) ;
    }
