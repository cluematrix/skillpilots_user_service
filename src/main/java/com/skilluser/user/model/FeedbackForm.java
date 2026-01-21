package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class FeedbackForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String fullName;
    private String mobileNumber;
    private String collegeName;
    private String course;
    private String currentYearOfStudy;
    private String overallSession;
    private String studentFriendly;
    private String exampleExplanation;
    private String sessionLikeInFuture;
    private String improveFutureSession;
    private String careerGrowthAndDirection;
    private String touchInFutureForProgram;
    private String freeTrialOfPsychoTest;
    private String anyDoubts;
    private LocalDate createdDate;
    private LocalDate updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDate.now();
    }
}
