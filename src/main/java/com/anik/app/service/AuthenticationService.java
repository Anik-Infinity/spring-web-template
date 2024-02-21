package com.anik.app.service;

import com.anik.app.dto.auth.AuthenticationRequestDto;
import com.anik.app.dto.auth.AuthenticationResponseDto;
import com.anik.app.dto.auth.RegisterRequestDto;
import com.anik.app.entity.user.Token;
import com.anik.app.entity.user.User;
import com.anik.app.enums.TokenType;
import com.anik.app.exception.ResourceNotFoundException;
import com.anik.app.repository.TokenRepository;
import com.anik.app.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterRequestDto request) {
        User user = User.builder()
              .firstName(request.getFirstName())
              .lastName(request.getLastName())
              .email(request.getEmail())
              .password(this.passwordEncoder.encode(request.getPassword()))
              .role(request.getRole())
              .build();
        User savedUser = this.userRepository.save(user);
        String jwtToken = this.jwtService.generateToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponseDto.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager
              .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = this.jwtService.generateToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDto.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
              .user(user)
              .token(jwtToken)
              .tokenType(TokenType.BEARER)
              .expired(false)
              .revoked(false)
              .build();
        this.tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = this.tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        this.tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new BadRequestException();
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail == null) throw new ResourceNotFoundException();
        User user = this.userRepository.findByEmail(userEmail).orElseThrow();
        if (!this.jwtService.isTokenValid(refreshToken, user)) throw new BadRequestException();
        String accessToken = this.jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return AuthenticationResponseDto.builder()
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .build();
    }
}

