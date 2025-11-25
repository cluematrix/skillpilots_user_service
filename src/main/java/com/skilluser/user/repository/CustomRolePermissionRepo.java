package com.skilluser.user.repository;

import com.skilluser.user.model.CustomRole;
import com.skilluser.user.model.CustomRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomRolePermissionRepo extends JpaRepository<CustomRolePermission, Long> {
    Optional<CustomRolePermission> findByCustomRoleIdAndModuleIdAndPath(Long customRoleId, Long moduleId, String path);
}
