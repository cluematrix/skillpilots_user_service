package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.Notification;
import com.skilluser.user.repository.NotificationRepository;
import com.skilluser.user.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    // delete notifications from database
    @Override
    @Transactional
    public void deleteOld(LocalDateTime date) {
        int count = notificationRepository.deleteAllOld(date);
        System.out.println("Deleted rows: " + count);
    }



    // fetch all notification by user id
    @Override
    @Transactional
    public List<Notification> getUnread(Long userId) {
        return notificationRepository
                .findByReceiverIdAndReadStatusFalseOrderByCreatedAtDesc(userId);
    }
}
