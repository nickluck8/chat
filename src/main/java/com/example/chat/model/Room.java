package com.example.chat.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rooms")
public record Room(
        @Id
        String id,
        @NotBlank(message = "Name must not be blank") String name
) {
}
