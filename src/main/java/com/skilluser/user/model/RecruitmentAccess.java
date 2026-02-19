package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"userId", "placementId"}
        )
)
public class RecruitmentAccess
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long collegeId;

    private Long placementId;

    private LocalDate startDate;

    private LocalDate expiryDate;

    private Boolean active = true;

    @PrePersist
    @PreUpdate
    public void setDefaultDates() {

        LocalDate today = LocalDate.now();

        // Case 1: Both null
        if (startDate == null && expiryDate == null) {
            this.startDate = today;
            this.expiryDate = today.plusMonths(1);
        }
        // Case 2: Start present, expiry null
        else if (startDate != null && expiryDate == null) {
            this.expiryDate = startDate.plusMonths(1);
        }
        // Case 3: Start null, expiry present
        else if (startDate == null && expiryDate != null) {
            this.startDate = today;
        }

        // Safety check
        if (expiryDate != null && startDate != null
                && expiryDate.isBefore(startDate)) {
            throw new RuntimeException("Expiry date cannot be before start date");
        }
    }

}
