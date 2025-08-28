package com.skilluser.user.serviceImpl;

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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public ModulePermission addModulePermission(ModulePermissionDTO request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module not found"));

        // Check if permission already exists
        // Check if permission already exists
        ModulePermission permission = modulePermissionRepository
                .findByRoleIdAndModuleId(role.getId(), module.getId())
                .orElse(new ModulePermission());

        permission.setRole(role);
        permission.setModule(module);
        permission.setCanView(request.isCanView());
        permission.setCanAdd(request.isCanAdd());
        permission.setCanEdit(request.isCanEdit());
        permission.setCanDelete(request.isCanDelete());

     return    modulePermissionRepository.save(permission);

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
                  list.add(module);
              }

        return list;
    }
}
