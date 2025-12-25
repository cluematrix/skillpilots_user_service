package com.skilluser.user.model.psychomatrictest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "psychometric_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychometricResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private PsychometricAttempt attempt;

    @Column(columnDefinition = "TEXT")
    private String aiSummary;
    private Long userId;

    @CurrentTimestamp
    private LocalDateTime generatedAt;
}
