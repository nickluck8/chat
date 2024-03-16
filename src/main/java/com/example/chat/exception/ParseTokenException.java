package com.example.chat.exception;

public class ParseTokenException extends RuntimeException {
    public ParseTokenException() {
        super("Failed to parse token");
    }
}
