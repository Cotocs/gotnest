package com.gotnest.buyer.service;

import com.gotnest.buyer.dto.JayChatInitResponseDTO;
import com.gotnest.buyer.dto.JayMessageDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class JayAssistantService {

    private static final String WELCOME_MESSAGE =
        "Hello! My name is Jay. I will help you with this. Tell me more about you";

    public JayChatInitResponseDTO initializeChat() {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        JayMessageDTO welcomeMessage = JayMessageDTO.builder()
                .message(WELCOME_MESSAGE)
                .sender("jay")
                .timestamp(now)
                .build();

        return JayChatInitResponseDTO.builder()
                .sessionId(sessionId)
                .welcomeMessage(welcomeMessage)
                .initiatedAt(now)
                .build();
    }
}

