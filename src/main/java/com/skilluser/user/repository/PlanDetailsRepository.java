package com.skilluser.user.repository;

import com.skilluser.user.model.PlanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanDetailsRepository extends JpaRepository<PlanDetails,Long> {
    Optional<PlanDetails> findByCollegeIdIsNull();

    Optional<PlanDetails> findByCollegeId(Long collegeId);
}
