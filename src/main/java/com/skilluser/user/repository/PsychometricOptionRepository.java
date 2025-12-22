package com.skilluser.user.repository;

import com.skilluser.user.model.psychomatrictest.PsychometricOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface PsychometricOptionRepository extends JpaRepository<PsychometricOption,Long> {
    List<PsychometricOption> findByQuestionId(Long questionId);
}
