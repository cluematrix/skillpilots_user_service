package com.skilluser.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "college_master")
public class CollegeMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "college_id")
    private Long collegeId;

    @Column(name = "college_name", nullable = false, unique = true)
    private String collegeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private StateMaster state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private UniversityMaster university;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "type") // Government / Private / Autonomous
    private String type;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "status", columnDefinition = "VARCHAR(50) DEFAULT 'Active'")
    private String status;
}
