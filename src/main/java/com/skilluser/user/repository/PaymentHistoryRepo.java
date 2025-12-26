package com.skilluser.user.repository;

import com.skilluser.user.model.PaymentHistory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentHistoryRepo extends JpaRepository<PaymentHistory, Long> {


    @Query("SELECT p FROM PaymentHistory p WHERE p.userId = :userId AND p.status <> 'success' ORDER BY p.paymentDate DESC limit 1")
    Optional<PaymentHistory> findLatestPendingPaymentByUserId(Long userId);

    Optional<PaymentHistory> findByOrderId(String orderId);
    @Query("SELECT p.receiptNumber FROM PaymentHistory p WHERE p.receiptNumber LIKE CONCAT('SP/', :financialYear, '/%') ORDER BY p.receiptNumber DESC LIMIT 1")
    String findLastReceiptNumber(@Param("financialYear") String financialYear);

    PaymentHistory findTopByUserIdOrderByPaymentDateDesc(Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PaymentHistory p where p.orderId = :orderId")
    Optional<PaymentHistory> lockByOrderId(@Param("orderId") String orderId);
  public  PaymentHistory findTopByUserIdAndStatusOrderByPaymentDateDesc(Long studentId, String success);


}
