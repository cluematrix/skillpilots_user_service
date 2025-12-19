package com.skilluser.user.repository;

import com.skilluser.user.model.psychomatrictest.PsychometricAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PsychometricAttemptRepository extends JpaRepository<PsychometricAttempt, Long> {
}
