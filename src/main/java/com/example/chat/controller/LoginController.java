package com.example.chat.controller;

import com.example.chat.dto.JwtAuthResponse;
import com.example.chat.dto.LoginDto;
import com.example.chat.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/api/v1/login")
    public Mono<ResponseEntity<JwtAuthResponse>> login(@RequestBody @Valid LoginDto loginDto) {
        return authService.login(loginDto)
                .map(token -> {
                    JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
                    jwtAuthResponse.setAccessToken(token);
                    return ResponseEntity.ok(jwtAuthResponse);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
