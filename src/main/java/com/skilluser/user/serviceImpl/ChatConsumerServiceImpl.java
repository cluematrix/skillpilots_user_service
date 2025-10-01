package com.skilluser.user.serviceImpl;

import com.skilluser.user.configuration.ChatMemory;
import com.skilluser.user.configuration.RabbitMQConfig;
import com.skilluser.user.model.LiveChatMessage;
import com.skilluser.user.service.ChatConsumerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

// 1/10/2025 - Ashvin chopkar
@Service
public class ChatConsumerServiceImpl implements ChatConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMemory chatMemory;

    public ChatConsumerServiceImpl(SimpMessagingTemplate messagingTemplate, ChatMemory chatMemory) {
        this.messagingTemplate = messagingTemplate;
        this.chatMemory = chatMemory;
    }

    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
    @Override
    public void receiveMessage(LiveChatMessage message) {
        String roomId = message.getRoomId() == null ? "public" : message.getRoomId();
        chatMemory.addMessage(roomId, message);

        if (message.getReceiverId() != null) {
            messagingTemplate.convertAndSendToUser(message.getReceiverId(), "/queue/messages", message);
        } else {
            messagingTemplate.convertAndSend("/topic/" + roomId, message);
        }
    }
}
