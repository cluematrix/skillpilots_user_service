package com.skilluser.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateResponseDTO
{
    private Long stateId;
    private String stateName;
    private String country;
    private List<CollegeSummaryDTO> colleges;
}
