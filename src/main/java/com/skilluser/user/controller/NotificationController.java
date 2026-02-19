package com.skilluser.user.controller;

import com.skilluser.user.model.Notification;
import com.skilluser.user.repository.NotificationRepository;
import com.skilluser.user.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 28/10/2025 - Ashvin Chopkar
@RestController
@RequestMapping("/api/v1/users/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    // get user notification by user id
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }



    // get all unread notifications - 23/01/2026
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // delete notifications from database one month old notification
        notificationService.deleteOld(oneMonthAgo);

        // fetch all notification by user id only latest notification within one month
        List<Notification> notifications = notificationService.getUnread(userId);

        return ResponseEntity.ok(notifications);
    }


    @Transactional
    private void deleteOld(LocalDateTime date) {
        int count = notificationRepository.deleteAllOld(date);
        System.out.println("Deleted rows: " + count);
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

//        // delete immediately
//        notificationRepository.delete(notification);

        // save in database
        notificationRepository.save(notification);

        response.put("message", "Notification read and saved successfully");
        return ResponseEntity.ok(response);
    }
}
