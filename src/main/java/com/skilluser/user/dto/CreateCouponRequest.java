package com.skilluser.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateCouponRequest {
    private String code;
    private double discountValue;       // 30 or 500

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private Integer maxUsage; // null = unlimited

    private Long businessUserId;
}
