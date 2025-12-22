package com.skilluser.user.model.psychomatrictest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "psychometric_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychometricTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private boolean active = true;

    @CurrentTimestamp
    private LocalDateTime createdAt;
}
