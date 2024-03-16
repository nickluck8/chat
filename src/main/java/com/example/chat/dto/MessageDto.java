package com.example.chat.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageDto(
        @NotBlank(message = "Room ID must not be blank")
        String roomId,
        @NotBlank(message = "Content must not be blank")
        String content
) {
}
