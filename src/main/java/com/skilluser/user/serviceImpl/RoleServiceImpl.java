package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.Role;
import com.skilluser.user.repository.RoleRepository;
import com.skilluser.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
}
