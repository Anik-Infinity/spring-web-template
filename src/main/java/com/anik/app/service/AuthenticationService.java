package com.anik.app.service;

import com.anik.app.entity.user.Token;
import com.anik.app.entity.user.User;
import com.anik.app.enums.TokenType;
import com.anik.app.repository.TokenRepository;
import com.anik.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void saveUserToken(User user, String jwtToken) {
        this.tokenRepository.save(Token.builder()
              .user(user)
              .token(jwtToken)
              .expired(false)
              .revoked(false)
              .tokenType(TokenType.BEARER)
              .build());
    }

    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = this.tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        this.tokenRepository.saveAll(validUserTokens);
    }
}