package com.dailymuse.microserviceauthentication.auth;

import com.dailymuse.microserviceauthentication.config.exeption.BadTokenException;
import com.dailymuse.microserviceauthentication.config.exeption.ExceptionMessageHandler;
import com.dailymuse.microserviceauthentication.config.exeption.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/ping")
    public String ping(@RequestBody RegisterRequest request) {
        return "pong";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistsException ex) {
            String errorMessage = ExceptionMessageHandler.USER_ALREADY_EXISTS;
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            String errorMessage = ExceptionMessageHandler.BAD_CREDENTIALS;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (DisabledException ex) {
            String errorMessage = ExceptionMessageHandler.ACCOUNT_DISABLED;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (LockedException ex) {
            String errorMessage = ExceptionMessageHandler.ACCOUNT_LOCKED;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(
            @RequestParam String token
    ) {
        try {
            Long response = service.getUserIdFromToken(token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
        catch (BadTokenException ex) {
            String errorMessage = ExceptionMessageHandler.BAD_TOKEN;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
}
