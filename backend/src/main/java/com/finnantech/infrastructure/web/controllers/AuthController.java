package com.finnantech.infrastructure.web.controllers;

import com.finnantech.application.usecases.AuthenticateUserUseCase;
import com.finnantech.application.usecases.RegisterUserUseCase;
import com.finnantech.infrastructure.web.dtos.AuthResponse;
import com.finnantech.infrastructure.web.dtos.LoginRequest;
import com.finnantech.infrastructure.web.dtos.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
@Tag(name = "Authentication", description = "Endpoints para autenticação (login e registro)")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    @Autowired
    public AuthController(RegisterUserUseCase registerUserUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário no sistema")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            RegisterUserUseCase.RegisterUserResponse response = registerUserUseCase.execute(
                    new RegisterUserUseCase.RegisterUserRequest(request.name(), request.email(), request.password())
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.success(
                    null, response.id(), response.name(), response.email(), response.emailVerified(), "Cadastro realizado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AuthResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Autentica o usuário e retorna um token JWT")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        try {
            AuthenticateUserUseCase.AuthenticateUserResponse response = authenticateUserUseCase.execute(
                    new AuthenticateUserUseCase.AuthenticateUserRequest(request.email(), request.password())
            );
            return ResponseEntity.ok(AuthResponse.success(
                    response.token(), response.userId(), response.name(), response.email(), response.emailVerified(), "Login realizado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.error(e.getMessage()));
        }
    }
} 