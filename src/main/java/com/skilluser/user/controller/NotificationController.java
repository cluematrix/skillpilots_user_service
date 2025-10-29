package com.skilluser.user.controller;

import com.skilluser.user.model.Notification;
import com.skilluser.user.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 28/10/2025 - Ashvin Chopkar
@RestController
@RequestMapping("/api/v1/users/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    // get user notification by user id
    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
    }



    // get all unread notifications
    @GetMapping("/unread/{userId}")
    public List<Notification> getUnreadNotifications(@PathVariable Long userId) {
        return notificationRepository.findByReceiverIdAndReadStatusFalseOrderByCreatedAtDesc(userId);
    }


    // update read status when user seen notification
    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
}
