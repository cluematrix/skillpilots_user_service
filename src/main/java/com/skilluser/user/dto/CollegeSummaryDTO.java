package com.skilluser.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeSummaryDTO
{
    private Long collegeId;
    private String collegeName;
    private String city;
    private String district;
    private String type;
}
