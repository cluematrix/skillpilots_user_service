package com.skilluser.user.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 30/09/2025 - Ashvin Chopkar - configure RbbitMQ
@Configuration
public class RabbitMQConfig {

    public static final String CHAT_QUEUE = "chat-queue";

    @Bean
    public Queue chatQueue() {
        // non-durable so messages are ephemeral; set true for persistence
        return new Queue(CHAT_QUEUE, false);
    }

    // JSON converter so RabbitTemplate can send/receive LiveChatMessage as JSON
    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
