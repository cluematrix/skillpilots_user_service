package com.skilluser.user.model;

import com.skilluser.user.enums.MessageStatus;
import com.skilluser.user.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


// 30/09/2025 - Ashvin Chopkar
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LiveChatMessage {

    private String id;
    private String senderId;
    private String receiverId;
    private String roomId;
    private String content;

    private LocalDateTime createdAt;
    private MessageStatus status;
    private MessageType type;
    private List<String> attachments;
    private Map<String,String> reaction;
}
