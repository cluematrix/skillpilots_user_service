package com.skilluser.user.service;

import com.skilluser.user.dto.CreateCouponRequest;
import com.skilluser.user.model.Coupon;
import com.skilluser.user.model.PlanDetails;
import com.skilluser.user.repository.PlanDetailsRepository;

public interface CouponService {

    public Coupon createCoupon(CreateCouponRequest request);

    public double applyCoupon(
            String couponCode,
            Long userId,
            double amount
    );
    public void confirmPaymentAndConsumeCoupon(
            String couponCode,
            Long userId
    );

}
