package com.dailymuse.microserviceauthentication.auth;

import com.dailymuse.microserviceauthentication.config.DefaultSettings;
import com.dailymuse.microserviceauthentication.config.JwtService;
import com.dailymuse.microserviceauthentication.config.exeption.BadTokenException;
import com.dailymuse.microserviceauthentication.config.exeption.UserAlreadyExistsException;
import com.dailymuse.microserviceauthentication.config.exeption.UserDoesNotExistException;
import com.dailymuse.microserviceauthentication.token.Token;
import com.dailymuse.microserviceauthentication.token.TokenRepository;
import com.dailymuse.microserviceauthentication.token.TokenType;
import com.dailymuse.microserviceauthentication.user.Role;
import com.dailymuse.microserviceauthentication.user.User;
import com.dailymuse.microserviceauthentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request)
            throws UserAlreadyExistsException {
        if (repository.findByEmail(request.getEmail()).isPresent())
            throw new UserAlreadyExistsException(
                    "User with email " + request.getEmail() +  " already exists"
            );
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        user.setSettings(DefaultSettings.getDefaultSettings(user));
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
            throws AuthenticationException
            {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public Long getUserIdFromToken(String jwtToken) throws UserDoesNotExistException, BadTokenException {
        final String userEmail = jwtService.extractUsername(jwtToken);
        if (userEmail != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                Optional<User> userOptional = repository.findByEmail(userEmail);
                if (userOptional.isPresent()) return userOptional.get().getId();
            } else {
                throw new BadTokenException("Token is not valid");
            }
        } else {
            throw new BadTokenException("Token is not valid");
        }
        return null;
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
