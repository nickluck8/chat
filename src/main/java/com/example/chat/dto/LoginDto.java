package com.example.chat.dto;


import jakarta.validation.constraints.Size;

public record LoginDto(
        @Size(min = 3, message = "Username must be at least 3 characters long")
        String username,
        @Size(min = 5, message = "Password must be at least 5 characters long")
        String password
) {
}