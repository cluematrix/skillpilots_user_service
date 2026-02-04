package com.skilluser.user.controller;

import com.skilluser.user.model.Notification;
import com.skilluser.user.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 28/10/2025 - Ashvin Chopkar
@RestController
@RequestMapping("/api/v1/users/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    // get user notification by user id
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }



    // get all unread notifications - 23/01/2026
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndReadStatusFalseOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }


    // mark as read / update read status when user seen notification and delete it from database - 23/01/2026
    @PutMapping("/read/{notificationId}/{userId}")
    public ResponseEntity<Map<String,String>> markAsRead(@PathVariable Long notificationId,
                                                         @PathVariable Long userId)
    {
        Map<String,String> response = new HashMap<>();

        Notification notification = notificationRepository
                .findByNotificationIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        // mark read (logical)
        notification.setReadStatus(true);

        // delete immediately
        notificationRepository.delete(notification);

        response.put("message", "Notification read and removed successfully");
        return ResponseEntity.ok(response);
    }
}
