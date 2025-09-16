package com.skilluser.user.dto;

import com.skilluser.user.enums.ServiceType;
import lombok.Data;

@Data
public class ModulePermissionDTO {

    private Long roleId;
    private Long moduleId;
    private boolean canView;
    private boolean canAdd;
    private boolean canEdit;
    private boolean canDelete;
    private ServiceType serviceType;

}
