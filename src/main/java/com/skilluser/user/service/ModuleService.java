package com.skilluser.user.service;

import com.skilluser.user.dto.ModulePermissionBulkDTO;
import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.dto.ModulePermissionGet;
import com.skilluser.user.model.Module;
import com.skilluser.user.model.ModulePermission;

import java.util.List;

public interface ModuleService {
    public Module createModule(Module module);

    public ModulePermission findByName(String name);
    public List<ModulePermissionDTO> addModulePermissions(ModulePermissionBulkDTO request);

    List<ModulePermissionGet> getModulePermission(Long userId);
}
