package com.skilluser.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

    private String type;
    private String message;
    private List<String> targetRoles;
    private Long collegeId;
    private Long deptId;
    private Long studentId;
    private Long companyId;
}
