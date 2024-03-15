package com.example.chat.controller;

import com.example.chat.dto.JwtAuthResponse;
import com.example.chat.dto.LoginDto;
import com.example.chat.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private LoginController loginController;

    @Test
    public void testLoginSuccess() {
        LoginDto loginDto = new LoginDto("username", "password");
        String token = "generated_token";
        when(authService.login(loginDto)).thenReturn(token);
        ResponseEntity<JwtAuthResponse> responseEntity = loginController.login(loginDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(token, responseEntity.getBody().getAccessToken());
        verify(authService, times(1)).login(loginDto);
    }

    @Test
    public void testLoginException() {
        LoginDto loginDto = new LoginDto("username", "password");
        when(authService.login(loginDto)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> loginController.login(loginDto));
        verify(authService, times(1)).login(loginDto);
    }
}
