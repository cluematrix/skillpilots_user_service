package com.skilluser.user.dto;

import lombok.Data;

import java.util.List;
@Data
public class LoginResponse {

    private Long userId;
    private String username;
    private String email;
    private Long department;
    private Long contact_no;
    private int hodverified = 0;

    private Long companyId;
    private int collegeId;
    private String photo;

    private List<String> user_role;

    public LoginResponse(String username, String email, Long department, Long contact_no, int hodverified, Long companyId, int collegeId, List<String> user_role) {
        this.username = username;
        this.email = email;
        this.department = department;
        this.contact_no = contact_no;
        this.hodverified = hodverified;
        this.companyId = companyId;
        this.collegeId = collegeId;
        this.user_role = user_role;
    }

    public LoginResponse( ) {

    }
}
