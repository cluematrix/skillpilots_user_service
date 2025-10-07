package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class ModulePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Role role;
    @ManyToOne
    private Module module;
    private boolean canView;
    private boolean canAdd;
    private boolean canEdit;
    private boolean canDelete;
    private String path;
    private Long collegeId; // nullable
    private Long companyId; // nullable
    private boolean isDefault = false; // true means admin default
    private String createdAt;

    @PrePersist
    public void createdAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdAt = LocalDate.now().format(formatter);
    }
}
