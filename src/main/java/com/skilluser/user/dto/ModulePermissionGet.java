package com.skilluser.user.dto;

import com.skilluser.user.enums.ServiceType;
import lombok.Data;

@Data
public class ModulePermissionGet {

    private String name;
    private String modalName;
    private boolean canView;
    private boolean canAdd;
    private boolean canEdit;
    private boolean canDelete;
    private String path;
    private ServiceType serviceType;

}
