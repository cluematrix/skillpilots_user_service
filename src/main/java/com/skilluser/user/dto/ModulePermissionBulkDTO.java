package com.skilluser.user.dto;

import lombok.Data;

import java.util.List;
@Data
public class ModulePermissionBulkDTO {

    private Long roleId;
    private List<ModulePermissionDTO> permissions;
}
