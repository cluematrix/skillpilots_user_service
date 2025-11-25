package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.CollegeSummaryDTO;
import com.skilluser.user.dto.StateResponseDTO;
import com.skilluser.user.dto.UniversityResponseDTO;
import com.skilluser.user.model.StateMaster;
import com.skilluser.user.model.UniversityMaster;
import com.skilluser.user.repository.StateMasterRepository;
import com.skilluser.user.service.StateMasterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StateMasterServiceImpl implements StateMasterService
{

    private final StateMasterRepository stateMasterRepository;

    @Override
    public StateMaster saveState(StateMaster stateMaster)
    {
        return stateMasterRepository.save(stateMaster);
    }

    @Override
    public StateResponseDTO updateState(StateMaster stateMaster)
    {
        StateMaster existing = stateMasterRepository.findById(stateMaster.getStateId())
                .orElseThrow(() -> new RuntimeException("University not found with ID: " + stateMaster.getStateId()));

        if (stateMaster.getStateName() != null)
        {
            existing.setStateName(stateMaster.getStateName());
        }
        if (stateMaster.getCountry() != null)
        {
            existing.setCountry(stateMaster.getCountry());
        }

        StateMaster updated = stateMasterRepository.save(existing);

        StateResponseDTO dto = new StateResponseDTO();
        dto.setStateId(updated.getStateId());
        dto.setStateName(updated.getStateName());
        dto.setCountry(updated.getCountry());

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
    public List<StateResponseDTO> getAllStates()
    {
        return stateMasterRepository.findAll().stream().map(state -> {
            StateResponseDTO dto = new StateResponseDTO();
            dto.setStateId(state.getStateId());
            dto.setStateName(state.getStateName());
            dto.setCountry(state.getCountry());

            if (state.getColleges() != null)
            {
                dto.setColleges(
                        state.getColleges().stream().map(college -> new CollegeSummaryDTO(
                                college.getCollegeId(),
                                college.getCollegeName(),
                                college.getCity(),
                                college.getDistrict(),
                                college.getType()
                        )).collect(Collectors.toList())
                );
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteState(Long stateId)
    {
        StateMaster state = stateMasterRepository.findById(stateId)
                .orElseThrow(() -> new RuntimeException("State not found:" + stateId));
        stateMasterRepository.delete(state);
    }


}
