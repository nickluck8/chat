package com.example.chat.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.chat.dto.MessageDto;
import com.example.chat.model.Message;
import com.example.chat.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @Test
    public void testGetAllMessages() {
        String roomId = "room1";
        int page = 0;
        int size = 10;
        Page<Message> expectedMessages = mock(Page.class);
        when(messageService.getAllMessagesByRoomId(roomId, page, size)).thenReturn(expectedMessages);
        ResponseEntity<Page<Message>> responseEntity = messageController.getAllMessages(roomId, page, size);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedMessages, responseEntity.getBody());
    }

    @Test
    public void testSendMessage() {
        MessageDto messageDto = new MessageDto("roomId", "message");
        Message expectedMessage = new Message();
        when(messageService.sendMessage(messageDto)).thenReturn(expectedMessage);
        Message result = messageController.sendMessage(messageDto);
        assertEquals(expectedMessage, result);
    }
}
