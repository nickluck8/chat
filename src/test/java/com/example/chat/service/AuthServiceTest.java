package com.example.chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.chat.dto.LoginDto;
import com.example.chat.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testLogin_Success() {

        LoginDto loginDto = new LoginDto("username", "password");
        Authentication authentication = mock(Authentication.class);
        String token = "generated_token";
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);


        String result = authService.login(loginDto);


        assertEquals(token, result);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtTokenProvider, times(1)).generateToken(authentication);
    }

    @Test
    public void testLogin_BadCredentialsException() {

        LoginDto loginDto = new LoginDto("username", "password");
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        assertThrows(BadCredentialsException.class, () -> authService.login(loginDto));
        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    public void testLogin_NullToken() {

        LoginDto loginDto = new LoginDto("username", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(null);


        String result = authService.login(loginDto);


        assertNull(result);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtTokenProvider, times(1)).generateToken(authentication);
    }
}
