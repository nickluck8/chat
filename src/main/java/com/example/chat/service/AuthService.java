package com.example.chat.service;

import com.example.chat.dto.LoginDto;
import com.example.chat.security.CustomUserDetailsService;
import com.example.chat.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthService {
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public Mono<String> login(LoginDto loginDto) {
        return userDetailsService.findByUsername(loginDto.username())
                .flatMap(userDetails -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, loginDto.password(), userDetails.getAuthorities());
                    return authenticationManager.authenticate(authentication)
                            .flatMap(authenticated -> ReactiveSecurityContextHolder.getContext()
                                    .doOnNext(securityContext -> {
                                        securityContext.setAuthentication(authenticated);
                                    })
                                    .thenReturn(jwtTokenProvider.generateToken(authenticated)));
                });
    }
}
