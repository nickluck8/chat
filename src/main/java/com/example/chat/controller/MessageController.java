package com.example.chat.controller;

import com.example.chat.dto.MessageDto;
import com.example.chat.model.Message;
import com.example.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<Page<Message>> getAllMessages(
            @RequestParam(value = "roomId") String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Message> messages = messageService.getAllMessagesByRoomId(roomId, page, size);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public Message sendMessage(@RequestBody MessageDto message) {
        return messageService.sendMessage(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable("id") String messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok("Message deleted successfully.");
    }
}
