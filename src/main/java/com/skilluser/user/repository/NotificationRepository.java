package com.skilluser.user.repository;

import com.skilluser.user.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndReadStatusFalseOrderByCreatedAtDesc(Long receiverId);

    Optional<Notification> findByNotificationIdAndReceiverId(Long notificationId, Long receiverId);

}
