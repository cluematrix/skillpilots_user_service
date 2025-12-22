package com.skilluser.user.model.psychomatrictest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "psychometric_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychometricOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private PsychometricQuestion question;

    @Column(nullable = false)
    private String label;      // A, B, C, D

    @Column(nullable = false)
    private String optionText; // Option content
}

