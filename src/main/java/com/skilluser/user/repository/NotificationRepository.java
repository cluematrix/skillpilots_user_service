package com.skilluser.user.repository;

import com.skilluser.user.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndReadStatusFalseOrderByCreatedAtDesc(Long receiverId);

    Optional<Notification> findByNotificationIdAndReceiverId(Long notificationId, Long receiverId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value =
            "DELETE FROM notification WHERE created_at < :date",
            nativeQuery = true)
    int deleteAllOld(@Param("date") LocalDateTime date);




}
