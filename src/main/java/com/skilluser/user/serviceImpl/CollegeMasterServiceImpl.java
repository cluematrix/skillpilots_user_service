package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.CollegeRequestDTO;
import com.skilluser.user.dto.CollegeResponseDTO;
import com.skilluser.user.model.*;
import com.skilluser.user.repository.*;
import com.skilluser.user.service.CollegeMasterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CollegeMasterServiceImpl implements CollegeMasterService
{

    private final CollegeMasterRepository collegeRepo;
    private final StateMasterRepository stateRepo;
    private final UniversityMasterRepository universityRepo;

    @Override
    public CollegeResponseDTO saveCollege(CollegeRequestDTO dto)
    {
        StateMaster state = stateRepo.findById(dto.getStateId())
                .orElseThrow(() -> new RuntimeException("Invalid State ID"));
        UniversityMaster university = universityRepo.findById(dto.getUniversityId())
                .orElseThrow(() -> new RuntimeException("Invalid University ID"));

        CollegeMaster college = new CollegeMaster();
        college.setCollegeName(dto.getCollegeName());
        college.setCity(dto.getCity());
        college.setDistrict(dto.getDistrict());
        college.setType(dto.getType());
        college.setState(state);
        college.setUniversity(university);
        college.setStatus("Active");

        CollegeMaster saved = collegeRepo.save(college);

        return new CollegeResponseDTO(
                saved.getCollegeId(),
                saved.getCollegeName(),
                saved.getCity(),
                saved.getDistrict(),
                saved.getType(),
                saved.getStatus(),
                state.getStateId(),
                state.getStateName(),
                university.getUniversityId(),
                university.getUniversityName()
        );
    }

    @Override
    public CollegeResponseDTO updateCollege(CollegeRequestDTO dto)
    {
        CollegeMaster college = collegeRepo.findById(dto.getCollegeId())
                .orElseThrow(() -> new RuntimeException("College not found with ID: " + dto.getCollegeId()));

        if (dto.getCollegeName() != null)
        {
            college.setCollegeName(dto.getCollegeName());
        }
        if (dto.getCity() != null)
        {
            college.setCity(dto.getCity());
        }
        if (dto.getDistrict() != null)
        {
            college.setDistrict(dto.getDistrict());
        }
        if (dto.getType() != null)
        {
            college.setType(dto.getType());
        }

        if (dto.getStateId() != null)
        {
            StateMaster state = stateRepo.findById(dto.getStateId())
                    .orElseThrow(() -> new RuntimeException("Invalid State ID"));
            college.setState(state);
        }

        if (dto.getUniversityId() != null)
        {
            UniversityMaster university = universityRepo.findById(dto.getUniversityId())
                    .orElseThrow(() -> new RuntimeException("Invalid University ID"));
            college.setUniversity(university);
        }

        CollegeMaster updated = collegeRepo.save(college);

        return new CollegeResponseDTO(
                updated.getCollegeId(),
                updated.getCollegeName(),
                updated.getCity(),
                updated.getDistrict(),
                updated.getType(),
                updated.getStatus(),
                updated.getState().getStateId(),
                updated.getState().getStateName(),
                updated.getUniversity().getUniversityId(),
                updated.getUniversity().getUniversityName()
        );
    }

    @Override
    public List<CollegeResponseDTO> getAllColleges()
    {
        return collegeRepo.findAll().stream().map(college -> new CollegeResponseDTO(
                college.getCollegeId(),
                college.getCollegeName(),
                college.getCity(),
                college.getDistrict(),
                college.getType(),
                college.getStatus(),
                college.getState() != null ? college.getState().getStateId() : null,
                college.getState() != null ? college.getState().getStateName() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityId() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityName() : null
        )).toList();
    }

    @Override
    public List<CollegeResponseDTO> getCollegesByState(Long stateId)
    {
        List<CollegeMaster> colleges = collegeRepo.findByState_StateId(stateId);

        if (colleges.isEmpty())
        {
            throw new RuntimeException("No colleges found for state ID: " + stateId);
        }

        return colleges.stream().map(college -> new CollegeResponseDTO(
                college.getCollegeId(),
                college.getCollegeName(),
                college.getCity(),
                college.getDistrict(),
                college.getType(),
                college.getStatus(),
                college.getState() != null ? college.getState().getStateId() : null,
                college.getState() != null ? college.getState().getStateName() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityId() : null,
                college.getUniversity() != null ? college.getUniversity().getUniversityName() : null
        )).toList();
    }

    @Override
    public List<CollegeResponseDTO> getCollegesByUniversity(Long universityId)
    {
        List<CollegeMaster> colleges = collegeRepo.findByUniversity_UniversityId(universityId);

        if (colleges.isEmpty())
        {
            throw new RuntimeException("No colleges found for university ID: " + universityId);
        }

        return colleges.stream().map(college -> new CollegeResponseDTO(
                college.getCollegeId(),
                college.getCollegeName(),
                college.getCity(),
                college.getDistrict(),
                college.getType(),
                college.getStatus(),
                college.getState().getStateId(),
                college.getState().getStateName(),
                college.getUniversity().getUniversityId(),
                college.getUniversity().getUniversityName()
        )).toList();
    }
}
