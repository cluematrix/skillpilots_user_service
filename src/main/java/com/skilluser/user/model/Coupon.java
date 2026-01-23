package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;        // ANIKET30
    private double discount;
    private boolean active;
    private LocalDate expiryDate;

    private Integer maxUsage;   // 100, 50, null = unlimited
    private Integer usedCount;// 30
    @ManyToOne
    private BusinessUser businessUser;
    @CurrentTimestamp
    private LocalDateTime createdAt;

}
