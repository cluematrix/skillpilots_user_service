package com.skilluser.user.service;

import com.skilluser.user.dto.UniversityResponseDTO;
import com.skilluser.user.model.UniversityMaster;

import java.util.List;

public interface UniversityMasterService
{
    public UniversityMaster saveUniversity(UniversityMaster universityMaster);

    public UniversityResponseDTO updateUniversity(UniversityMaster universityMaster);

    public List<UniversityResponseDTO> getAllUniversities();

    public List<UniversityResponseDTO> getUniversitiesByState(Long stateId);

    public void deleteUniversity(Long universityId);
}
