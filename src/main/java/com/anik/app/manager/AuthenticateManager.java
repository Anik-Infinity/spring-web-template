package com.anik.app.manager;

import com.anik.app.dto.auth.AuthenticationResponseDto;
import com.anik.app.dto.auth.UserLogInDto;
import com.anik.app.dto.auth.UserRegistrationDto;
import com.anik.app.entity.user.User;
import com.anik.app.exception.ResourceNotFoundException;
import com.anik.app.mapper.UserMapper;
import com.anik.app.service.AuthenticationService;
import com.anik.app.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticateManager {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(UserRegistrationDto registrationDto) {
        User user = this.userMapper.toEntity(registrationDto);
        user.setPassword(this.passwordEncoder.encode(registrationDto.getPassword()));
        User savedUser = this.authenticationService.save(user);
        String jwtToken = this.jwtService.generateToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);
        this.authenticationService.saveUserToken(savedUser, jwtToken);
        AuthenticationResponseDto responseDto = this.userMapper.toDto(user);
        responseDto.setAccessToken(jwtToken);
        responseDto.setRefreshToken(refreshToken);
        return responseDto;
    }

    public AuthenticationResponseDto authenticate(UserLogInDto request) {
        authenticationManager
              .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = this.authenticationService.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = this.jwtService.generateToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);
        this.authenticationService.revokeAllUserTokens(user);
        this.authenticationService.saveUserToken(user, jwtToken);
        AuthenticationResponseDto responseDto = this.userMapper.toDto(user);
        responseDto.setAccessToken(jwtToken);
        responseDto.setRefreshToken(refreshToken);
        return responseDto;
    }

    public AuthenticationResponseDto refreshToken(HttpServletRequest request) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new BadRequestException();
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) throw new BadRequestException();
        User user = this.authenticationService.findByEmail(userEmail).orElseThrow(ResourceNotFoundException::new);
        if (!this.jwtService.isTokenValid(refreshToken, user)) throw new BadRequestException();
        String accessToken = this.jwtService.generateToken(user);
        this.authenticationService.revokeAllUserTokens(user);
        this.authenticationService.saveUserToken(user, accessToken);
        return AuthenticationResponseDto.builder().accessToken(accessToken).build();
    }

}
