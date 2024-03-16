package com.example.chat.exception;

public final class MessageNotFoundException extends RuntimeException {
    private static final String MESSAGE_NOT_FOUND = "Message not found";

    public MessageNotFoundException() {
        super(MESSAGE_NOT_FOUND);
    }
}
