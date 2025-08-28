package com.skilluser.user.repository;

import com.skilluser.user.model.ModulePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ModulePermissionRepository extends JpaRepository<ModulePermission,Long> {
    Optional<ModulePermission> findByRoleIdAndModuleId(Long roleId, Long moduleId);

    List<ModulePermission> findByRoleId(Long id);
    // public ModulePermission findByName(String name);
}
