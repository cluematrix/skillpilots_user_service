package com.skilluser.user.repository;

import com.skilluser.user.model.psychomatrictest.PsychometricTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface PsychometricTestRepository extends JpaRepository<PsychometricTest, Long> {
    Page<PsychometricTest> findAll(Pageable pageable);
}
