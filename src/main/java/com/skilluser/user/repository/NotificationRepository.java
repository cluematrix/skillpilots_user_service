package com.skilluser.user.repository;

import com.skilluser.user.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndReadStatusFalseOrderByCreatedAtDesc(Long receiverId);


}
