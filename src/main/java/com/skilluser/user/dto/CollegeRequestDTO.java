package com.skilluser.user.dto;

import lombok.Data;

@Data
public class CollegeRequestDTO {
    private Long collegeId;
    private String collegeName;
    private String city;
    private String district;
    private String type;
    private Long stateId;
    private Long universityId;
}
