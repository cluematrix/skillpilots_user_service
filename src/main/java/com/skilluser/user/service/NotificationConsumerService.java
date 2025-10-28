package com.skilluser.user.service;

import com.skilluser.user.model.NotificationEvent;

public interface NotificationConsumerService {

    void receiveNotification(NotificationEvent event);
}
