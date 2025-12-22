package com.skilluser.user.model.psychomatrictest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "psychometric_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychometricAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(optional = false)
    @JsonIgnore
    private PsychometricTest test;

    private boolean submitted = false;

    @CurrentTimestamp
    private LocalDateTime startedAt;
}
