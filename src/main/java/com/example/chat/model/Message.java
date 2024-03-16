package com.example.chat.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Data
public class Message {
    @Id
    private String id;

    @NotBlank(message = "Room ID must not be blank")
    private String roomId;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    private String username;

    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotNull(message = "Timestamp must not be null")
    private Long timestamp;
}
