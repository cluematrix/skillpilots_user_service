package com.skilluser.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityResponseDTO
{
    private Long universityId;
    private String universityName;
    private String address;

    private List<CollegeSummaryDTO> colleges;
}
