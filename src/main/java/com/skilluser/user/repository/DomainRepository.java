package com.skilluser.user.repository;

import com.skilluser.user.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DomainRepository extends JpaRepository<Domain, UUID> {
}
