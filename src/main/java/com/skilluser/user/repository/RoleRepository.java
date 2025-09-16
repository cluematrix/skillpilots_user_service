package com.skilluser.user.repository;

import com.skilluser.user.enums.ServiceType;
import com.skilluser.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);

    Optional<Role> findByNameAndServiceType(String name, ServiceType serviceType);
}
