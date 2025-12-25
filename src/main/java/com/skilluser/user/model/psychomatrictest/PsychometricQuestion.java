package com.skilluser.user.model.psychomatrictest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skilluser.user.enums.QuestionType;
import com.skilluser.user.enums.TestSection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "psychometric_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychometricQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JsonIgnore
    private PsychometricTest test;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestSection section;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(nullable = false, length = 1000)
    private String questionText;

    private boolean isDelete;
}
