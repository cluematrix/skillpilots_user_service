package com.skilluser.user.repository;

import com.skilluser.user.model.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactRequest ,Long> {
}
