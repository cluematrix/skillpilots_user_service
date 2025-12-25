package com.skilluser.user.repository;

import com.skilluser.user.model.psychomatrictest.PsychometricResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychometricResultRepository extends JpaRepository<PsychometricResult,Long> {

    List<PsychometricResult> findByUserIdOrderByGeneratedAtDesc(Long userId);
}
