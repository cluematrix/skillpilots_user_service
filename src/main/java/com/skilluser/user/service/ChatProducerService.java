package com.skilluser.user.service;

import com.skilluser.user.model.LiveChatMessage;

public interface ChatProducerService {

    void sendMessage(LiveChatMessage message);
}
