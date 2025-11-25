package com.skilluser.user.dto;

import lombok.Data;

@Data
public class PermissionRequest {
    private Long moduleId;
    private String path;
    private boolean canView;
    private boolean canAdd;
    private boolean canEdit;
    private boolean canDelete;
}
