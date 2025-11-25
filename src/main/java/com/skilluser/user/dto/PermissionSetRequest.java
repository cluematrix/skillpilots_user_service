package com.skilluser.user.dto;

import lombok.Data;

import java.util.List;
@Data
public class PermissionSetRequest {
    private Long roleId;
    private Long collegeId;
    private Long companyId;
    private List<PermissionRequest> permissions;
}
