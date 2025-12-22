package com.skilluser.user.repository;

import com.skilluser.user.model.psychomatrictest.PsychometricResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PsychometricResultRepository extends JpaRepository<PsychometricResult,Long> {
}
