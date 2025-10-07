package com.skilluser.user.repository;

import com.skilluser.user.model.CustomRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRoleRepository extends JpaRepository<CustomRole,Long> {
}
