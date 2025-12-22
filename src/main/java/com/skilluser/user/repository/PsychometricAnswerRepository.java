package com.skilluser.user.repository;

import com.skilluser.user.model.psychomatrictest.PsychometricAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychometricAnswerRepository extends JpaRepository<PsychometricAnswer,Long> {
    List<PsychometricAnswer> findByAttemptId(Long id);
}
