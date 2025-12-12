package com.gotnest.jay.controller;

import com.gotnest.jay.dto.JayChatInitResponseDTO;
import com.gotnest.jay.service.JayAssistantService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/bff/v1/jay")
@RequiredArgsConstructor
public class JayAssistantController {

    private final JayAssistantService jayAssistantService;

    @PostMapping(path = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<@NonNull JayChatInitResponseDTO> initializeChat() {
        return Mono.fromCallable(jayAssistantService::initializeChat);
    }
}

