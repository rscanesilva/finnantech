package com.finnantech.application.usecases;

import com.finnantech.domain.entities.User;
import com.finnantech.domain.ports.UserRepositoryPort;
import com.finnantech.domain.ports.PasswordServicePort;
import com.finnantech.domain.valueobjects.Email;
import com.finnantech.application.exceptions.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para registro de usuário local
 */
@Service
public class RegisterUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordServicePort passwordService;
    
    public RegisterUserUseCase(UserRepositoryPort userRepository, 
                              PasswordServicePort passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }
    
    public RegisterUserResponse execute(RegisterUserRequest request) {
        // Validar se email já existe
        Email email = Email.of(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Usuário já existe com este email: " + request.email());
        }
        
        // Hash da senha
        String hashedPassword = passwordService.hashPassword(request.password());
        
        // Criar usuário
        User user = User.createLocalUser(request.name(), email, hashedPassword);
        
        // Salvar usuário
        User savedUser = userRepository.save(user);
        
        return new RegisterUserResponse(
            savedUser.getId().getValue(),
            savedUser.getName(),
            savedUser.getEmail().getValue(),
            savedUser.isEmailVerified()
        );
    }
    
    public record RegisterUserRequest(
        String name,
        String email,
        String password
    ) {}
    
    public record RegisterUserResponse(
        String id,
        String name,
        String email,
        boolean emailVerified
    ) {}
} 