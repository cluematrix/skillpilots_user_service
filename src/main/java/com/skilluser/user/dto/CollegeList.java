package com.skilluser.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeList {
    private Long collegeId;
    private String collegeName;
    private String type;
    private Boolean isRegistered;
}
