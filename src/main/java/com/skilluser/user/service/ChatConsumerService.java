package com.skilluser.user.service;

import com.skilluser.user.model.LiveChatMessage;

public interface ChatConsumerService {

    void receiveMessage(LiveChatMessage message);
}
