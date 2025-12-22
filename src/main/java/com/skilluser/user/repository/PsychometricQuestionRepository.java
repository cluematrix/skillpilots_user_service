package com.skilluser.user.repository;

import com.skilluser.user.enums.TestSection;
import com.skilluser.user.model.psychomatrictest.PsychometricQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychometricQuestionRepository extends JpaRepository<PsychometricQuestion,Long> {
    List<PsychometricQuestion> findByTestIdAndSection(Long id, TestSection section);
}
