package com.skilluser.user.controller;

import com.skilluser.user.configuration.ChatMemory;
import com.skilluser.user.enums.MessageStatus;
import com.skilluser.user.enums.MessageType;
import com.skilluser.user.model.LiveChatMessage;
import com.skilluser.user.service.ChatProducerService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// 30/09/2025 && 1/10/2025 - Ashvin Chopkar - create chatController and add some rest api
@Controller
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatProducerService chatProducerService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMemory chatMemory;


    public ChatController(ChatProducerService chatProducerService,
                          SimpMessagingTemplate messagingTemplate,
                          ChatMemory chatMemory) {
        this.chatProducerService = chatProducerService;
        this.messagingTemplate = messagingTemplate;
        this.chatMemory = chatMemory;
    }

    // REST send message (optional)
    @PostMapping("/sendMessage")
    @ResponseBody
    public String sendMessageREST(@RequestBody LiveChatMessage message) {
        if (message.getId() == null) message.setId(UUID.randomUUID().toString());
        message.setCreatedAt(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        chatMemory.addMessage(message.getRoomId(), message);

        chatProducerService.sendMessage(message);
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
        return "Message sent!";
    }

    @MessageMapping("/sendMessage") // handles WebSocket messages from frontend
    public void sendMessageWS(@Payload LiveChatMessage message) {
        if (message.getId() == null) message.setId(UUID.randomUUID().toString());
        message.setCreatedAt(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        chatMemory.addMessage(message.getRoomId(), message);

        String room = message.getRoomId() == null ? "public" : message.getRoomId();
        messagingTemplate.convertAndSend("/topic/" + room, message);
    }


    // Join room
    @MessageMapping("/addUser")
    public void addUser(@Payload LiveChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSenderId());
        String room = message.getRoomId() == null ? "public" : message.getRoomId();

        // Send join notice to room
        LiveChatMessage joinMessage = new LiveChatMessage();
        joinMessage.setId(UUID.randomUUID().toString());
        joinMessage.setSenderId("Server");
        joinMessage.setRoomId(room);
        joinMessage.setContent(message.getSenderId() + " joined the room");
        joinMessage.setType(MessageType.JOIN);
        joinMessage.setCreatedAt(LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/" + room, joinMessage);

        // Send last messages in memory to new user
        List<LiveChatMessage> lastMessages = chatMemory.getMessages(room);
        ((List<?>) lastMessages).forEach(msg -> messagingTemplate.convertAndSendToUser(
                message.getSenderId(), "/queue/messages", msg
        ));
    }


    // Typing indicator
    @MessageMapping("/typing")
    public void typing(@Payload LiveChatMessage message) {
        String room = message.getRoomId() == null ? "public" : message.getRoomId();
        message.setType(MessageType.TYPING);
        messagingTemplate.convertAndSend("/topic/" + room + "/typing", message);
    }

    @MessageMapping("/privateMessage")
    public void privateMessage(@Payload LiveChatMessage message) {
        message.setCreatedAt(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        message.setType(MessageType.TEXT);

        // Generate the same private room topic as frontend
        String privateRoom = "private-" + Arrays.asList(message.getSenderId(), message.getReceiverId())
                .stream().sorted().collect(Collectors.joining("-"));

        chatMemory.addMessage(privateRoom, message);

        messagingTemplate.convertAndSend("/topic/" + privateRoom, message);
    }


    // delivered message
    @MessageMapping("/delivered")
    public void delivered(@Payload LiveChatMessage message) {
        message.setStatus(MessageStatus.DELIVERED);
        chatMemory.updateMessageStatus(message.getId(), MessageStatus.DELIVERED);
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
    }


    @MessageMapping("/seen")
    public void seen(@Payload LiveChatMessage message) {
        message.setStatus(MessageStatus.SEEN);
        chatMemory.updateMessageStatus(message.getId(), MessageStatus.SEEN);
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
    }


    @MessageMapping("/reaction")
    public void addReaction(@Payload Map<String, String> reactionData) {
        String messageId = reactionData.get("messageId");
        String userId = reactionData.get("userId");
        String reaction = reactionData.get("reaction");

        LiveChatMessage reactionMsg = new LiveChatMessage();
        reactionMsg.setId(UUID.randomUUID().toString());
        reactionMsg.setSenderId(userId);
        reactionMsg.setContent("Reacted with : " + reaction);
        reactionMsg.setType(MessageType.EMOJI);
        reactionMsg.setCreatedAt(LocalDateTime.now());
        reactionMsg.setStatus(MessageStatus.SENT);

        messagingTemplate.convertAndSend("/topic/" + reactionData.get("roomId"), reactionMsg);
    }


    @MessageMapping("/mediaMessage")
    public void mediaMessage(@Payload LiveChatMessage message, MessageStatus status) {
        if(message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
            message.setCreatedAt(LocalDateTime.now());
            message.setStatus(MessageStatus.SENT); // Always set SENT by default

            if (message.getType() == null) {
                message.setType(MessageType.FILE);
            }

            chatMemory.addMessage(message.getRoomId(), message);
            messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);

            if(message.getType() == null) {
                message.setType(MessageType.FILE);
            }

            // Determine destination room: private or public
            String destinationRoom;
            if(message.getReceiverId() != null && !message.getReceiverId().isEmpty()) {
                destinationRoom = "private-" + Arrays.asList(message.getSenderId(), message.getReceiverId())
                        .stream().sorted().collect(Collectors.joining("-"));
            } else {
                destinationRoom = message.getRoomId() == null ? "public" : message.getRoomId();
            }

            chatMemory.addMessage(destinationRoom, message);
            messagingTemplate.convertAndSend("/topic/" + destinationRoom, message);

        }
    }


}
