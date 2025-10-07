package com.skilluser.user.configuration;

import com.skilluser.user.enums.MessageStatus;
import com.skilluser.user.model.LiveChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// 1/10/2025 - Ashvin Chopkar - create chat memory
@Component
public class ChatMemory {

    private Map<String, List<LiveChatMessage>> roomMessages = new ConcurrentHashMap<>();

    public void addMessage(String roomId, LiveChatMessage message) {
        roomMessages.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(message);
        // Keep only last 50 messages
        List<LiveChatMessage> messages = roomMessages.get(roomId);
        if (messages.size() > 50) {
            messages.remove(0);
        }
    }

    public List<LiveChatMessage> getMessages(String roomId) {
        return roomMessages.getOrDefault(roomId, new CopyOnWriteArrayList<>());
    }

//update status of message
    public boolean updateMessageStatus(String messageId, MessageStatus newStatus) {
        for (List<LiveChatMessage> messages : roomMessages.values()) {
            for (LiveChatMessage msg : messages) {
                if (msg.getId().equals(messageId)) {
                    msg.setStatus(newStatus);
                    return true; // updated successfully
                }
            }
        }
        return false; // not found
    }
}
