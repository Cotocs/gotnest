package com.gotnest.jay.controller;

import com.gotnest.jay.dto.JayChatInitResponseDTO;
import com.gotnest.jay.service.JayAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/jay")
@RequiredArgsConstructor
public class JayAssistantController {

    private final JayAssistantService jayAssistantService;

    @PostMapping("/chat/init")
    public Mono<ResponseEntity<JayChatInitResponseDTO>> initializeChat() {
        JayChatInitResponseDTO response = jayAssistantService.initializeChat();
        return Mono.just(ResponseEntity.ok(response));
    }
}

