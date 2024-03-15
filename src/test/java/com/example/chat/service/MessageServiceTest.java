package com.example.chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.chat.dto.MessageDto;
import com.example.chat.model.Message;
import com.example.chat.repository.MessageRepository;
import com.example.chat.security.JwtTokenProvider;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MessageServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void init() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetAllMessagesByRoomId() {
        String roomId = "roomId";
        int page = 0;
        int size = 10;
        Page<Message> expectedMessages = mock(Page.class);
        when(messageRepository.findByRoomId(roomId, PageRequest.of(page, size))).thenReturn(expectedMessages);

        Page<Message> result = messageService.getAllMessagesByRoomId(roomId, page, size);

        assertEquals(expectedMessages, result);
        verify(messageRepository, times(1)).findByRoomId(roomId, PageRequest.of(page, size));
    }

    @Test
    public void testSendMessage() {
        MessageDto messageDto = new MessageDto("roomId", "content");
        Message expectedMessage = new Message();
        expectedMessage.setRoomId("roomId");
        expectedMessage.setUsername("username");
        expectedMessage.setContent("content");
        expectedMessage.setTimestamp(LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        when(messageRepository.save(any())).thenReturn(expectedMessage);

        Message result = messageService.sendMessage(messageDto);

        assertEquals(expectedMessage, result);
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    public void testExistsById() {

        String messageId = "messageId";
        when(messageRepository.existsById(messageId)).thenReturn(true);

        boolean result = messageService.existsById(messageId);

        assertTrue(result);
        verify(messageRepository, times(1)).existsById(messageId);
    }

    @Test
    public void testIsOwner_ExistingMessage_Owner() {
        String messageId = "messageId";
        String username = "username";
        Message message = new Message();
        message.setUsername(username);
        Optional<Message> messageOptional = Optional.of(message);
        when(messageRepository.findById(messageId)).thenReturn(messageOptional);
        when(messageRepository.existsById(messageId)).thenReturn(true);
        boolean result = messageService.isOwner(messageId);
        assertTrue(result);
        verify(messageRepository, times(1)).findById(messageId);
    }

    @Test
    public void testIsOwner_ExistingMessage_NotOwner() {
        String messageId = "messageId";
        Message message = new Message();
        message.setUsername("otherUser");
        Optional<Message> messageOptional = Optional.of(message);
        when(messageRepository.findById(messageId)).thenReturn(messageOptional);
        boolean result = messageService.isOwner(messageId);
        assertFalse(result);
        verify(messageRepository, times(1)).findById(messageId);
    }

    @Test
    public void testIsOwner_NonExistingMessage() {
        String messageId = "nonExistingMessageId";
        when(messageRepository.existsById(messageId)).thenReturn(false);
        boolean result = messageService.isOwner(messageId);
        assertFalse(result);
        verify(messageRepository).findById(messageId);
    }

    @Test
    public void testDeleteMessage() {
        String messageId = "messageId";
        messageService.deleteMessage(messageId);
        verify(messageRepository, times(1)).deleteById(messageId);
    }
}
