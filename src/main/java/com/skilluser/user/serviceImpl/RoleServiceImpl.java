package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.ModulePermissionDTO;
import com.skilluser.user.model.CustomRole;
import com.skilluser.user.model.Module;
import com.skilluser.user.model.ModulePermission;
import com.skilluser.user.model.Role;
import com.skilluser.user.repository.CustomRoleRepository;
import com.skilluser.user.repository.ModulePermissionRepository;
import com.skilluser.user.repository.ModuleRepository;
import com.skilluser.user.repository.RoleRepository;
import com.skilluser.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final ModulePermissionRepository modulePermissionRepository;
    private final CustomRoleRepository customRoleRepository;

    public RoleServiceImpl(RoleRepository roleRepository, ModuleRepository moduleRepository, ModulePermissionRepository modulePermissionRepository, CustomRoleRepository customRoleRepository) {
        this.roleRepository = roleRepository;
        this.moduleRepository = moduleRepository;
        this.modulePermissionRepository = modulePermissionRepository;
        this.customRoleRepository = customRoleRepository;
    }

    @Override
    public Role createRole(Role role) {
        // Check if role already exists with same name + serviceType
        Optional<Role> existing = roleRepository.findByNameAndServiceType(role.getName(), role.getServiceType());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Role '" + role.getName() + "' already exists for serviceType: " + role.getServiceType());
        }

        role.setActive(true);
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role updateRole(Long id, Role updatedRole) {
       return roleRepository.findById(id).map(existingRole-> {
           existingRole.setName(updatedRole.getName());
           existingRole.setServiceType(updatedRole.getServiceType());
           existingRole.setActive(updatedRole.isActive());
         return   roleRepository.save(existingRole);

       }).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }


    @Override
    public void deleteRole(Long id) {
        roleRepository.findById(id)
                .ifPresent(role -> roleRepository.deleteById(id));
    }

    @Override
    public void savePermissions(Role role, List<ModulePermissionDTO> permissions, boolean isDefault, Long collegeId, Long companyId) {
        List<ModulePermission> modulePermissions = permissions.stream().map(p -> {
            Module module = moduleRepository.findById(p.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));
            ModulePermission mp = new ModulePermission();
            mp.setRole(role);
            mp.setModule(module);
            mp.setCanView(p.isCanView());
            mp.setCanAdd(p.isCanAdd());
            mp.setCanEdit(p.isCanEdit());
            mp.setCanDelete(p.isCanDelete());
            mp.setDefault(isDefault);
            mp.setCollegeId(collegeId);
            mp.setCompanyId(companyId);
            mp.setPath(module.getPath());
            return mp;
        }).collect(Collectors.toList());

        modulePermissionRepository.saveAll(modulePermissions);
    }

    @Override
    public CustomRole createCustomRole(CustomRole customRole) {
        return customRoleRepository.save( customRole);
    }
}
