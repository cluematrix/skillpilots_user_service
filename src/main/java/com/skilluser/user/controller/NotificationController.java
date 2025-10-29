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



    // get all unread notifications
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndReadStatusFalseOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }


    // update read status when user seen notification
    @PutMapping("/read/{id}")
    public ResponseEntity<Map<String,String>> markAsRead(@PathVariable Long id) {
        Map<String,String> responce = new HashMap<>();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
        responce.put("message", "user seen notification successfully!");
        return ResponseEntity.ok(responce);
    }
}
