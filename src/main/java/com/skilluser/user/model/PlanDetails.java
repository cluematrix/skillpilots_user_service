package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Data
@Entity
public class PlanDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;
    private String user;
    private String planName;
    private String description;
    private double amount;

    private double totalAmt;

    private String createdAt;

    private Long collegeId;

    @PrePersist
    private void createdAt() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdAt = LocalDate.now().format(dateTimeFormatter);
    }
}