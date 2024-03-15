package com.example.chat.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Test
    public void testDeleteMessageNotFound() {
        String messageId = "123";
        when(messageService.existsById(messageId)).thenReturn(false);
        ResponseEntity<String> responseEntity = messageController.deleteMessage(messageId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Message not found", responseEntity.getBody());
        verify(messageService, never()).deleteMessage(messageId);
    }

    @Test
    public void testDeleteMessageForbidden() {
        String messageId = "123";
        when(messageService.existsById(messageId)).thenReturn(true);
        when(messageService.isOwner(messageId)).thenReturn(false);
        ResponseEntity<String> responseEntity = messageController.deleteMessage(messageId);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not allowed to delete this message.", responseEntity.getBody());
        verify(messageService, never()).deleteMessage(messageId);
    }

    @Test
    public void testDeleteMessageSuccess() {
        String messageId = "123";
        when(messageService.existsById(messageId)).thenReturn(true);
        when(messageService.isOwner(messageId)).thenReturn(true);
        ResponseEntity<String> responseEntity = messageController.deleteMessage(messageId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Message deleted successfully.", responseEntity.getBody());
        verify(messageService, times(1)).deleteMessage(messageId);
    }
}
