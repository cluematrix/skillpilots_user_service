package com.skilluser.user.service;

import com.skilluser.user.dto.UniversityResponseDTO;
import com.skilluser.user.model.UniversityMaster;

import java.util.List;

public interface UniversityMasterService
{
    UniversityMaster saveUniversity(UniversityMaster universityMaster);

    UniversityResponseDTO updateUniversity(UniversityMaster universityMaster);

    List<UniversityResponseDTO> getAllUniversities();

    List<UniversityResponseDTO> getUniversitiesByState(Long stateId);

    void deleteUniversity(Long universityId);
}
