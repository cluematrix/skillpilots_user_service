package com.skilluser.user.repository;

import com.skilluser.user.model.Coupon;
import com.skilluser.user.model.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUsageRepository extends JpaRepository<CouponUsage,Long> {
    boolean existsByCouponAndUserId(Coupon coupon, Long userId);
}
