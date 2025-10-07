package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CustomRolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
    // e.g. "STUDENT", "ATTENDANCE", "PLACEMENT"
    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
    private boolean canAdd;
    private String path;

    @ManyToOne
    @JoinColumn(name = "custom_role_id")
    private CustomRole customRole;
}
