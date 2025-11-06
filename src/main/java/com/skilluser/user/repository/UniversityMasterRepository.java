package com.skilluser.user.repository;

import com.skilluser.user.model.UniversityMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityMasterRepository extends JpaRepository<UniversityMaster,Long> {

    List<UniversityMaster> findByColleges_State_StateId(Long stateId);
}
