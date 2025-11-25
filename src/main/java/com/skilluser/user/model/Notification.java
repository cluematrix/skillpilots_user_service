package com.skilluser.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    private String message;
    private String type;
    private Long receiverId;
    private Long studentId;
    private Long collegeId;
    private Long deptId;
    private Long companyId;
    private boolean readStatus;
    private LocalDateTime createdAt = LocalDateTime.now();
}
