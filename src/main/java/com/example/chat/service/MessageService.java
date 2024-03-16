package com.example.chat.service;

import com.example.chat.dto.MessageDto;
import com.example.chat.exception.ForbiddenException;
import com.example.chat.exception.MessageNotFoundException;
import com.example.chat.model.Message;
import com.example.chat.repository.MessageRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Page<Message> getAllMessagesByRoomId(String roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timestamp"));
        return messageRepository.findByRoomId(roomId, pageable);
    }

    public Message sendMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setRoomId(messageDto.roomId());
        message.setUsername(getUsernameFromContext());
        message.setContent(messageDto.content());
        message.setTimestamp(LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        return messageRepository.save(message);
    }


    public boolean existsById(String messageId) {
        return messageRepository.existsById(messageId);
    }

    public boolean isOwner(String messageId) {
        String username = getUsernameFromContext();
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            return message.getUsername().equals(username);
        }
        return false;
    }

    public void deleteMessage(String messageId) {
        if (!existsById(messageId)) {
            throw new MessageNotFoundException();
        }

        if (!isOwner(messageId)) {
            throw new ForbiddenException("You are not allowed to delete this message.");
        }

        messageRepository.deleteById(messageId);
    }

    private String getUsernameFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
