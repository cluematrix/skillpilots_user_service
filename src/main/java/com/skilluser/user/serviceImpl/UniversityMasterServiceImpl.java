package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.CollegeSummaryDTO;
import com.skilluser.user.dto.UniversityResponseDTO;
import com.skilluser.user.model.UniversityMaster;
import com.skilluser.user.repository.UniversityMasterRepository;
import com.skilluser.user.service.UniversityMasterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UniversityMasterServiceImpl implements UniversityMasterService
{
    private final UniversityMasterRepository universityMasterRepository;

    @Override
    public UniversityMaster saveUniversity(UniversityMaster universityMaster)
    {
        return universityMasterRepository.save(universityMaster);
    }

    @Override
    public UniversityResponseDTO updateUniversity(UniversityMaster universityMaster)
    {
        UniversityMaster existing = universityMasterRepository.findById(universityMaster.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found with ID: " + universityMaster.getUniversityId()));

        if (universityMaster.getUniversityName() != null)
        {
            existing.setUniversityName(universityMaster.getUniversityName());
        }
        if (universityMaster.getAddress() != null)
        {
            existing.setAddress(universityMaster.getAddress());
        }

        UniversityMaster updated = universityMasterRepository.save(existing);

        UniversityResponseDTO dto = new UniversityResponseDTO();
        dto.setUniversityId(updated.getUniversityId());
        dto.setUniversityName(updated.getUniversityName());
        dto.setAddress(updated.getAddress());

        if (updated.getColleges() != null)
        {
            List<CollegeSummaryDTO> collegeList = updated.getColleges().stream()
                    .map(college -> new CollegeSummaryDTO(
                            college.getCollegeId(),
                            college.getCollegeName(),
                            college.getCity(),
                            college.getDistrict(),
                            college.getType()
                    ))
                    .toList();
            dto.setColleges(collegeList);
        }

        return dto;
    }

    @Override
    public List<UniversityResponseDTO> getAllUniversities()
    {
        return universityMasterRepository.findAll().stream().map(univ -> {
            UniversityResponseDTO dto = new UniversityResponseDTO();
            dto.setUniversityId(univ.getUniversityId());
            dto.setUniversityName(univ.getUniversityName());
            dto.setAddress(univ.getAddress());

          /*  if (univ.getColleges() != null)
            {
                dto.setColleges(
                        univ.getColleges().stream().map(college -> new CollegeSummaryDTO(
                                college.getCollegeId(),
                                college.getCollegeName(),
                                college.getCity(),
                                college.getDistrict(),
                                college.getType()
                        )).collect(Collectors.toList())
                );
            }*/
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UniversityResponseDTO> getUniversitiesByState(Long stateId)
    {
        List<UniversityMaster> universities =
                universityMasterRepository.findByColleges_State_StateId(stateId);

        if (universities.isEmpty())
        {
            throw new RuntimeException("No universities found for state ID: " + stateId);
        }

        return universities.stream().map(univ -> new UniversityResponseDTO(
                univ.getUniversityId(),
                univ.getUniversityName(),
                univ.getAddress(),
                null
        )).toList();
    }

    @Override
    public void deleteUniversity(Long universityId)
    {
        UniversityMaster university = universityMasterRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("University Not found :" + universityId));

        universityMasterRepository.delete(university);
    }

}
