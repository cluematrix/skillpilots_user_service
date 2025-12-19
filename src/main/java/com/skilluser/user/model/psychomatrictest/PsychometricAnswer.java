package com.skilluser.user.model.psychomatrictest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "psychometric_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychometricAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private PsychometricAttempt attempt;

    @ManyToOne(optional = false)
    private PsychometricQuestion question;

    // For TRUE/FALSE, YES/NO, MCQ
    private String selectedAnswer;

    // For DESCRIPTIVE questions
    @Column(columnDefinition = "TEXT")
    private String descriptiveAnswer;
}
