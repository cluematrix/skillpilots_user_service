package com.skilluser.user.dto;

import lombok.Data;

@Data
public class ModulePermissionDTO {

    private Long roleId;
    private Long moduleId;
    private boolean canView;
    private boolean canAdd;
    private boolean canEdit;
    private boolean canDelete;
}
