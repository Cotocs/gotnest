package com.gotnest.jay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JayMessageDTO {
    private String message;
    private String sender; // "jay" or "user"
    private LocalDateTime timestamp;
}

