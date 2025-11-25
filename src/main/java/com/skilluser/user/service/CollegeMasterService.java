package com.skilluser.user.service;

import com.skilluser.user.dto.CollegeRequestDTO;
import com.skilluser.user.dto.CollegeResponseDTO;
import com.skilluser.user.model.CollegeMaster;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CollegeMasterService
{
    public CollegeResponseDTO saveCollege(CollegeRequestDTO dto);
    public CollegeResponseDTO updateCollege(CollegeRequestDTO collegeRequestDTO);
    public List<CollegeResponseDTO> getAllColleges();
    public List<CollegeResponseDTO> getCollegesByState(Long stateId);
    public List<CollegeResponseDTO> getCollegesByUniversity(Long universityId);
    public Map<String, Object> uploadMasterData(MultipartFile file);
}
