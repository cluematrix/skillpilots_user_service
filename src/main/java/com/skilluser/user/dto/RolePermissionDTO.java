package com.skilluser.user.dto;

import java.util.List;

public class RolePermissionDTO {
    private String roleName; // Role name, e.g., HOD, Teacher
    private Long collegeId;  // Optional: set when creating a college-specific role
    private Long companyId;  // Optional: set when creating a company-specific role
    private List<ModulePermissionDTO> permissions;
}
