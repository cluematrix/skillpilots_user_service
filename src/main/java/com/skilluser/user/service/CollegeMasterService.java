package com.skilluser.user.service;

import com.skilluser.user.dto.CollegeRequestDTO;
import com.skilluser.user.dto.CollegeResponseDTO;
import com.skilluser.user.model.CollegeMaster;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CollegeMasterService
{
    CollegeResponseDTO saveCollege(CollegeRequestDTO dto);
    CollegeResponseDTO updateCollege(CollegeRequestDTO collegeRequestDTO);
    List<CollegeResponseDTO> getAllColleges();
    List<CollegeResponseDTO> getCollegesByState(Long stateId);
    List<CollegeResponseDTO> getCollegesByUniversity(Long universityId);
    Map<String, Object> uploadMasterData(MultipartFile file);
}
