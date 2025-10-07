package com.skilluser.user.serviceImpl;

import com.skilluser.user.configuration.RabbitMQConfig;
import com.skilluser.user.model.LiveChatMessage;
import com.skilluser.user.service.ChatProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

// 1/10/2025  - Ashvin chopkar
@Service
public class ChatProducerServiceImpl implements ChatProducerService {

    private final RabbitTemplate rabbitTemplate;

    public ChatProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(LiveChatMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_QUEUE,message);
    }
}
