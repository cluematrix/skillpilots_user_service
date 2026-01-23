package com.skilluser.user.dto;

import lombok.Data;

@Data
public class ApplyCoupon {
    String couponCode;
    Long userId;
    double amount;
}
