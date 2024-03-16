package com.example.chat;

import com.example.chat.model.Message;
import com.example.chat.model.Room;
import com.example.chat.model.User;
import com.example.chat.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@EnableMongoRepositories
@RequiredArgsConstructor
public class ChatApplication implements CommandLineRunner {
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) {

        userRepository.save(new User(1L, "user", passwordEncoder.encode("1")));
        Room room = new Room("default", "General");
        Room saved = mongoTemplate.save(room);
        Message message = new Message();
        message.setRoomId(saved.id());
        message.setUsername("user");
        message.setContent("Hello, world!");
        message.setTimestamp(LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        mongoTemplate.save(message);
    }
}
