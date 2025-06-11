package com.finnantech.domain.ports;

import com.finnantech.domain.entities.User;
import com.finnantech.domain.valueobjects.Email;
import com.finnantech.domain.valueobjects.UserId;
import java.util.Optional;

/**
 * Port (interface) para persistência de usuários
 * Define o contrato que deve ser implementado pela camada de infraestrutura
 */
public interface UserRepositoryPort {
    
    /**
     * Salva um usuário
     */
    User save(User user);
    
    /**
     * Busca usuário por ID
     */
    Optional<User> findById(UserId userId);
    
    /**
     * Busca usuário por email
     */
    Optional<User> findByEmail(Email email);
    
    /**
     * Busca usuário por provider e provider ID (para OAuth)
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    
    /**
     * Verifica se existe usuário com o email informado
     */
    boolean existsByEmail(Email email);
    
    /**
     * Deleta um usuário
     */
    void delete(User user);
} 