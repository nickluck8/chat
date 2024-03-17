package com.example.chat;

import com.example.chat.model.Message;
import com.example.chat.model.Room;
import com.example.chat.model.User;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.RoomRepository;
import com.example.chat.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class ChatApplication implements CommandLineRunner {
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) {

        userRepository.save(new User(1L, "user", passwordEncoder.encode("12345")));
        Room room = new Room("default", "General");
        Room saved = roomRepository.save(room).block();
        Message message = new Message();
        message.setRoomId(saved.id());
        message.setUsername("user");
        message.setContent("Hello, world!");
        message.setTimestamp(LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        messageRepository.save(message);
    }
}
