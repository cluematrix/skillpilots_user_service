package com.skilluser.user.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

// 30/09/2025 - Ashvin Chopkar - configure RbbitMQ for live chat module
// 22/10/2025 - Ashvin Chopkar - configure RabbitMQ for notification module
@Configuration
public class RabbitMQConfig {

    public static final String CHAT_QUEUE = "chat-queue";

    // for live chat
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



    // for notification module
    @Bean
    public TopicExchange exchange()
    {
        return  new TopicExchange("notification_exchange");
    }

    @Bean
    public Queue queue()
    {
        return new Queue("notification_queue");
    }

    @Bean
    public Binding binding(Queue queue,TopicExchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with("notify");
    }

    //  Fix: add mapping for all NotificationEvent package names
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();

        idClassMapping.put("com.student.model.NotificationEvent", com.skilluser.user.model.NotificationEvent.class);
        idClassMapping.put("com.company.model.NotificationEvent", com.skilluser.user.model.NotificationEvent.class);
        idClassMapping.put("com.skilluser.user.model.NotificationEvent", com.skilluser.user.model.NotificationEvent.class);

        classMapper.setIdClassMapping(idClassMapping);

        //  Allow deserialization from all relevant services
        classMapper.setTrustedPackages(
                "java.util",
                "java.lang",
                "com.student.model",
                "com.company.model",
                "com.skilluser.user.model"
        );

        converter.setClassMapper(classMapper);
        return converter;
    }

    //  Listener factory setup
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }


}
