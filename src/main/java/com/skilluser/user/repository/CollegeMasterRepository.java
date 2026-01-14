package com.skilluser.user.repository;

import com.skilluser.user.dto.CollegeResponseDTO;
import com.skilluser.user.model.CollegeMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollegeMasterRepository extends JpaRepository<CollegeMaster,Long> {
    List<CollegeMaster> findByState_StateId(Long stateId);
    List<CollegeMaster> findByUniversity_UniversityId(Long universityId);

}
