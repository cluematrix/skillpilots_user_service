package com.skilluser.user.repository;

import com.skilluser.user.model.psychomatrictest.PsychometricAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PsychometricAttemptRepository extends JpaRepository<PsychometricAttempt, Long> {
    List<PsychometricAttempt> findByUserIdAndSubmittedTrueOrderByStartedAtDesc(Long userId);

    Optional<PsychometricAttempt>
    findTopByUserIdAndSubmittedTrueOrderByStartedAtDesc(Long userId);
}
