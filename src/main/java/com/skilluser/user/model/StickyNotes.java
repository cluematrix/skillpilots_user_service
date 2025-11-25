package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "sticky_notes")
public class StickyNotes
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String notes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date = new Date();

    @Column(nullable = false)
    private Long userId;
}
