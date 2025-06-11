package com.finnantech.application.usecases;

import com.finnantech.domain.entities.User;
import com.finnantech.domain.ports.UserRepositoryPort;
import com.finnantech.domain.ports.PasswordServicePort;
import com.finnantech.domain.ports.JwtServicePort;
import com.finnantech.domain.valueobjects.Email;
import com.finnantech.application.exceptions.InvalidCredentialsException;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para autenticação de usuário local
 */
@Service
public class AuthenticateUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordServicePort passwordService;
    private final JwtServicePort jwtService;
    
    public AuthenticateUserUseCase(UserRepositoryPort userRepository,
                                 PasswordServicePort passwordService,
                                 JwtServicePort jwtService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtService = jwtService;
    }
    
    public AuthenticateUserResponse execute(AuthenticateUserRequest request) {
        // Buscar usuário por email
        Email email = Email.of(request.email());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("Credenciais inválidas"));
        
        // Verificar se é usuário local
        if (!user.isLocalUser()) {
            throw new InvalidCredentialsException("Este email está associado a login social");
        }
        
        // Verificar senha
        if (!passwordService.verifyPassword(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }
        
        // Gerar token JWT
        String token = jwtService.generateToken(user);
        
        return new AuthenticateUserResponse(
            token,
            user.getId().getValue(),
            user.getName(),
            user.getEmail().getValue(),
            user.isEmailVerified()
        );
    }
    
    public record AuthenticateUserRequest(
        String email,
        String password
    ) {}
    
    public record AuthenticateUserResponse(
        String token,
        String userId,
        String name,
        String email,
        boolean emailVerified
    ) {}
}