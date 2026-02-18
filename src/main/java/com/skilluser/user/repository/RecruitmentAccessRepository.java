package com.skilluser.user.repository;

import com.skilluser.user.model.RecruitmentAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentAccessRepository extends JpaRepository<com.skilluser.user.model.RecruitmentAccess,Long> {

    RecruitmentAccess findByUserIdAndPlacementId(Long userId, Long placementId);

    void deleteByPlacementId(Long placementId);

    RecruitmentAccess findByPlacementIdAndActiveTrue(Long placementId);

    RecruitmentAccess findByPlacementId(Long placementId);

    List<RecruitmentAccess> findByUserIdAndActiveTrue(Long userId);



}
