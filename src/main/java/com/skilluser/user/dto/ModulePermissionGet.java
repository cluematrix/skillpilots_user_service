package com.skilluser.user.dto;

import lombok.Data;

@Data
public class ModulePermissionGet {

    private String name;
    private String modalName;
    private boolean canView;
    private boolean canAdd;
    private boolean canEdit;
    private boolean canDelete;
}
