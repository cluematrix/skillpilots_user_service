package com.skilluser.user.repository;

import com.skilluser.user.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepo extends JpaRepository<PaymentStatus, Long> {


    PaymentStatus findByCollegeId(long collegeId);
}
