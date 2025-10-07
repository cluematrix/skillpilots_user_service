package com.skilluser.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private int collegeId;
    private Long contactNo;

}
