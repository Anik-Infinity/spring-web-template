package com.anik.app.controller;

import com.anik.app.dto.auth.UserLogInDto;
import com.anik.app.dto.auth.AuthenticationResponseDto;
import com.anik.app.manager.AuthenticateManager;
import com.anik.app.response.contract.ApiResponse;
import com.anik.app.response.success.ResponseUtils;
import com.anik.app.service.AuthenticationService;
import com.anik.app.dto.auth.UserRegistrationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticateManager authenticateManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegistrationDto registrationDto) {
        AuthenticationResponseDto responseDto = this.authenticateManager.register(registrationDto);
        return ResponseUtils.buildResponse(HttpStatus.CREATED, responseDto, "Created successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody UserLogInDto logInDto) {
        AuthenticationResponseDto responseDto = this.authenticateManager.authenticate(logInDto);
        return ResponseUtils.buildResponse(HttpStatus.OK, responseDto, "Authenticated successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(HttpServletRequest request) throws IOException {
        AuthenticationResponseDto responseDto = this.authenticateManager.refreshToken(request);
        return ResponseUtils.buildResponse(HttpStatus.OK, responseDto, "Issued new access token successfully");
    }

}
