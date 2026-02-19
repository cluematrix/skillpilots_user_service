package com.skilluser.user.service;

import com.skilluser.user.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {

    void deleteOld(LocalDateTime date);

    List<Notification> getUnread(Long userId);
}
