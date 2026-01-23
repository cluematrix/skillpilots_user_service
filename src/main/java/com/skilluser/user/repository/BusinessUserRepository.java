package com.skilluser.user.repository;

import com.skilluser.user.model.BusinessUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessUserRepository extends JpaRepository<BusinessUser,Long> {
}
