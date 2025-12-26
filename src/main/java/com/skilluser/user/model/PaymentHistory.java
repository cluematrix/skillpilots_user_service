package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    private String paymentType;  // e.g., "CASH", "ONLINE"
    @CurrentTimestamp
    private LocalDate paymentDate;
    private Long userId;
    private String receiptNumber;
    @Column(nullable = false, unique = true, updatable = false)
    private String orderId;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String status;
    private String gatewayTxnId;
    private String gatewayStatus;
    private double paidAmount;

    @Column(columnDefinition = "TEXT")
    private String rawResponse;


}
