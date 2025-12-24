package com.skilluser.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.stream.Collectors;

// 30/09/2025 - Ashvin chopkar - configure WebSocket
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // We use simple broker for STOMP endpoints; actual messages will be forwarded by backend consumer.
        registry.enableSimpleBroker("/topic","/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns(
//                        "http://localhost:[*]",  // Allows any port from localhost
//                        "http://4.240.102.47:[*]", // Allows any port from your IP
//                        "http://localhost:3000",  // Your React dev server
//                        "http://4.240.102.47:6060",
//                        "http://127.0.0.1:5500"// Your production frontend
////                        "http://localhost:[*]",
////                        "http://4.240.102.47:[*]",
////                        "null",        // Add this for local file:/// access
////                        "file://*"
//                )
                .setAllowedOriginPatterns("*");
    }


//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor =
//                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//                if (accessor != null && accessor.getCommand() != null) {
//                    // Handle CONNECT with token
//                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                        List<String> authHeaders = accessor.getNativeHeader("Authorization");
//
//                        if (authHeaders != null && !authHeaders.isEmpty()) {
//                            String authHeader = authHeaders.get(0);
//
//                            if (authHeader.startsWith("Bearer ")) {
//                                String token = authHeader.substring(7);
//
//                                try {
//                                    // Validate token
//                                    Long userId = jwtUtil.extractUserId(token);
//                                    if (userId != null && jwtUtil.validateToken(token, userId)) {
//                                        // Extract roles
//                                        List<String> roles = jwtUtil.extractRoles(token);
//                                        List<SimpleGrantedAuthority> authorities = roles.stream()
//                                                .map(SimpleGrantedAuthority::new)
//                                                .collect(Collectors.toList());
//
//                                        // Create authentication
//                                        UsernamePasswordAuthenticationToken auth =
//                                                new UsernamePasswordAuthenticationToken(
//                                                        userId.toString(),
//                                                        null,
//                                                        authorities
//                                                );
//                                        accessor.setUser(auth);
//                                    }
//                                } catch (Exception e) {
//                                    // Token validation failed
//                                }
//                            }
//                        }
//                    }
//                }
//                return message;
//            }
//        });
//    }
}
