package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "department_data")
public class Department {
    @Id
    @Column(name = "dept_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    private String dept_name;
    private String dept_type;
    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "updation_date")
    private Date updationDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updationDate = new Date();
    }
}