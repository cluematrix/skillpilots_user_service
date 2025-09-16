package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.ModulePermissionBulkDTO;
import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.dto.ModulePermissionGet;
import com.skilluser.user.model.Module;
import com.skilluser.user.model.ModulePermission;
import com.skilluser.user.model.Role;
import com.skilluser.user.model.User;
import com.skilluser.user.repository.ModulePermissionRepository;
import com.skilluser.user.repository.ModuleRepository;
import com.skilluser.user.repository.RoleRepository;
import com.skilluser.user.repository.UserRepository;
import com.skilluser.user.service.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final ModulePermissionRepository modulePermissionRepository;
    @Override
    public Module createModule(Module module) {
        return moduleRepository.save(module);
    }

    @Override
    public ModulePermission findByName(String name) {
        return null;// modulePermissionRepository.findByName(name);
    }

    public List<ModulePermissionDTO> addModulePermissions(ModulePermissionBulkDTO request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<ModulePermissionDTO> savedPermissions = new ArrayList<>();

        for (ModulePermissionDTO dto : request.getPermissions()) {
            Module module = moduleRepository.findById(dto.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));

            // Check if permission already exists
            ModulePermission permission = modulePermissionRepository
                    .findByRoleIdAndModuleId(role.getId(), module.getId())
                    .orElse(new ModulePermission());

            permission.setRole(role);
            permission.setModule(module);
            permission.setCanView(dto.isCanView());
            permission.setCanAdd(dto.isCanAdd());
            permission.setCanEdit(dto.isCanEdit());
            permission.setCanDelete(dto.isCanDelete());

         ModulePermission modulePermission= modulePermissionRepository.save(permission);
            ModulePermissionDTO modulePermissionDTO = getModulePermissionDTO(modulePermission);
            savedPermissions.add(modulePermissionDTO);
        }

        return savedPermissions;
    }

    private static ModulePermissionDTO getModulePermissionDTO(ModulePermission modulePermission) {
        ModulePermissionDTO modulePermissionDTO= new ModulePermissionDTO();
        modulePermissionDTO.setModuleId(modulePermission.getModule().getId());
        modulePermissionDTO.setServiceType(modulePermission.getModule().getServiceType());
        modulePermissionDTO.setRoleId(modulePermission.getRole().getId());
        modulePermissionDTO.setCanEdit(modulePermission.isCanEdit());
        modulePermissionDTO.setCanAdd(modulePermission.isCanAdd());
        modulePermissionDTO.setCanView(modulePermission.isCanView());
        modulePermissionDTO.setCanDelete(modulePermission.isCanDelete());
        return modulePermissionDTO;
    }

    @Override
    public List<ModulePermissionGet> getModulePermission(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        System.out.println("User"+ user);
        Role role = user.getRoles();
        ModulePermissionGet module = new ModulePermissionGet();
        ArrayList<ModulePermissionGet> list = new ArrayList<>();
        List<ModulePermission> permissions = modulePermissionRepository.findByRoleId(role.getId());
              for(ModulePermission m : permissions)  {
                  module.setName(m.getRole().getName());
                  module.setModalName(m.getModule().getName());
                  module.setCanView(m.isCanView());
                  module.setCanEdit(m.isCanEdit());
                  module.setCanAdd(m.isCanAdd());
                  module.setCanDelete(m.isCanDelete());
                  module.setPath(module.getPath());
                  list.add(module);
              }

        return list;
    }
}
