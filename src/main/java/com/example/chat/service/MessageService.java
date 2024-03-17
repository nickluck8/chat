package com.example.chat.service;

import com.example.chat.dto.MessageDto;
import com.example.chat.exception.ForbiddenException;
import com.example.chat.exception.MessageNotFoundException;
import com.example.chat.model.Message;
import com.example.chat.repository.MessageRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Mono<Page<Message>> getAllMessagesByRoomId(String roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timestamp"));
        return messageRepository.findByRoomId(roomId, pageable)
                .collectList()
                .flatMap(messages -> {
                    int totalMessages = messages.size();
                    int totalPages = (int) Math.ceil((double) totalMessages / size);
                    int start = Math.max(0, Math.min(page * size, totalMessages));
                    int end = Math.min(start + size, totalMessages);
                    List<Message> pageMessages = messages.subList(start, end);
                    return Mono.just(new PageImpl<>(pageMessages, pageable, totalMessages));
                });
    }

    public Mono<Message> sendMessage(MessageDto messageDto) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Message message = new Message();
                    message.setRoomId(messageDto.roomId());
                    message.setUsername(securityContext.getAuthentication().getName());
                    message.setContent(messageDto.content());
                    message.setTimestamp(LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
                    return message;
                })
                .flatMap(messageRepository::save);
    }

    public Mono<Boolean> existsById(String messageId) {
        return messageRepository.existsById(messageId);
    }

    public Mono<Boolean> isOwner(String messageId) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    String username = securityContext.getAuthentication().getName();
                    return messageRepository.findById(messageId)
                            .map(message -> message.getUsername().equals(username));
                });
    }

    public Mono<Void> deleteMessage(String messageId) {
        return existsById(messageId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new MessageNotFoundException());
                    }
                    return isOwner(messageId)
                            .flatMap(isOwner -> {
                                if (!isOwner) {
                                    return Mono.error(new ForbiddenException("You are not allowed to delete this message."));
                                }
                                return messageRepository.deleteById(messageId).then();
                            });
                });
    }

    public static Page<Message> createPage(List<Message> content, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());

        return new PageImpl<>(content.subList(start, end), pageable, content.size());
    }
}
