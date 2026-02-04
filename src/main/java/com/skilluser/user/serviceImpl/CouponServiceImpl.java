package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.CreateCouponRequest;
import com.skilluser.user.model.BusinessUser;
import com.skilluser.user.model.Coupon;
import com.skilluser.user.model.CouponUsage;
import com.skilluser.user.model.PlanDetails;
import com.skilluser.user.repository.BusinessUserRepository;
import com.skilluser.user.repository.CouponRepository;
import com.skilluser.user.repository.CouponUsageRepository;
import com.skilluser.user.service.CouponService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private BusinessUserRepository businessUserRepository;
    @Autowired
    private CouponUsageRepository couponUsageRepository;


    @Override
    public Coupon createCoupon(CreateCouponRequest request) {

        BusinessUser user = businessUserRepository.findById(request.getBusinessUserId())
                .orElseThrow(() -> new RuntimeException("Business user not found"));

        Coupon coupon;

        //
        if (request.getCode() != null && !request.getCode().isBlank()) {

            Optional<Coupon> existingOpt =
                    couponRepository.findByCode(request.getCode());

            if (existingOpt.isPresent()) {
                Coupon existing = existingOpt.get();

                // Active coupon with same code → BLOCK
                if (existing.isActive()) {
                    throw new RuntimeException("Coupon code already active");
                }

                // Inactive coupon → REUSE
                coupon = existing;
            } else {
                coupon = new Coupon();
                coupon.setCode(request.getCode().toUpperCase());
            }

        }
        //  CASE 2: Auto-generate code
        else {
            coupon = new Coupon();

            String autoCode;
            do {
                autoCode =
                        generate(user.getName(), request.getDiscountValue());
            } while (couponRepository.existsByCodeAndActiveTrue(autoCode));

            coupon.setCode(autoCode);
        }

        // Common fields (overwrite safely)
        coupon.setBusinessUser(user);
        coupon.setDiscount(request.getDiscountValue());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setMaxUsage(request.getMaxUsage()); // null = unlimited
        coupon.setUsedCount(0);
        coupon.setActive(true);

        return couponRepository.save(coupon);
    }

    @Override
    @Transactional
    public double applyCoupon(
            String couponCode,
            Long userId,
            double amount
    ) {

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        //  Active + Expiry check
        if (!coupon.isActive() || coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Coupon expired or inactive");
        }

        //  One-time per user check
        if (couponUsageRepository.existsByCouponAndUserId(coupon, userId)) {
            throw new RuntimeException("Coupon already used by this user");
        }

        //  Usage limit check (NULL = unlimited)
        if (coupon.getMaxUsage() != null &&
                coupon.getUsedCount() >= coupon.getMaxUsage()) {
            throw new RuntimeException("Coupon usage limit exceeded");
        }

        double realAmount = amount;

        double discountAmount = Math.floor(realAmount * coupon.getDiscount() / 100.0);
        double finalAmount = Math.floor(realAmount - discountAmount);



        System.out.println("Final Amount"+finalAmount + "realAmount"+ amount);
        finalAmount = Math.max(finalAmount, 0);

        System.out.println("Real Amount     = " + realAmount);
        System.out.println("Discount Amount = " + discountAmount);
        System.out.println("Final Amount    = " + finalAmount);
//        CouponUsage usage = new CouponUsage();
//        usage.setCoupon(coupon);
//        usage.setUserId(userId);
//        usage.setUsedAt(LocalDateTime.now());
//        couponUsageRepository.save(usage);

        //Increase count
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        return finalAmount;
    }
    @Override
    @Transactional
    public void confirmPaymentAndConsumeCoupon(
            String couponCode,
            Long userId
    ) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        // Double safety
        if (couponUsageRepository.existsByCouponAndUserId(coupon, userId)) {
            throw new RuntimeException("Coupon already consumed");
        }

        CouponUsage usage = new CouponUsage();
        usage.setCoupon(coupon);
        usage.setUserId(userId);
        usage.setUsedAt(LocalDateTime.now());

        couponUsageRepository.save(usage);

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);
    }


    public static String generate(String businessName, double discount) {
        String prefix = "SP";
        String name = businessName.substring(0, Math.min(4, businessName.length())).toUpperCase();
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        return prefix + "-" + name + "-" + (int) discount + "-" + random;
    }
}
