package com.example.chat.dto;


public record LoginDto(
        //can add validation
        String username,
        String password) {
}