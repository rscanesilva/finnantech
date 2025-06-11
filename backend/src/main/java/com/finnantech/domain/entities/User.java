package com.finnantech.domain.entities;

import com.finnantech.domain.valueobjects.Email;
import com.finnantech.domain.valueobjects.UserId;
import com.finnantech.domain.exceptions.InvalidUserDataException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade User do domínio
 * Contém toda a lógica de negócio relacionada ao usuário
 */
public class User {
    
    private UserId id;
    private String name;
    private Email email;
    private String passwordHash;
    private AuthProvider provider;
    private String providerId;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Construtor privado para garantir invariantes
    private User(UserId id, String name, Email email, String passwordHash, 
                 AuthProvider provider, String providerId, boolean emailVerified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.provider = provider;
        this.providerId = providerId;
        this.emailVerified = emailVerified;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        validateUser();
    }
    
    // Factory method para usuário local
    public static User createLocalUser(String name, Email email, String passwordHash) {
        validateLocalUserData(name, email, passwordHash);
        return new User(UserId.generate(), name, email, passwordHash, 
                       AuthProvider.LOCAL, null, false);
    }
    
    // Factory method para usuário OAuth
    public static User createOAuthUser(String name, Email email, AuthProvider provider, 
                                     String providerId, boolean emailVerified) {
        validateOAuthUserData(name, email, provider, providerId);
        return new User(UserId.generate(), name, email, null, 
                       provider, providerId, emailVerified);
    }
    
    // Factory method para reconstrução (ex: do banco de dados)
    public static User reconstruct(UserId id, String name, Email email, String passwordHash,
                                 AuthProvider provider, String providerId, boolean emailVerified,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        User user = new User(id, name, email, passwordHash, provider, providerId, emailVerified);
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        return user;
    }
    
    // Business methods
    public void verifyEmail() {
        this.emailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateProfile(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new InvalidUserDataException("Nome não pode ser vazio");
        }
        this.name = newName.trim();
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isLocalUser() {
        return AuthProvider.LOCAL.equals(this.provider);
    }
    
    public boolean isOAuthUser() {
        return !isLocalUser();
    }
    
    // Validations
    private void validateUser() {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserDataException("Nome é obrigatório");
        }
        if (email == null) {
            throw new InvalidUserDataException("Email é obrigatório");
        }
        if (provider == null) {
            throw new InvalidUserDataException("Provider é obrigatório");
        }
        if (isLocalUser() && (passwordHash == null || passwordHash.isEmpty())) {
            throw new InvalidUserDataException("Password hash é obrigatório para usuários locais");
        }
        if (isOAuthUser() && (providerId == null || providerId.isEmpty())) {
            throw new InvalidUserDataException("Provider ID é obrigatório para usuários OAuth");
        }
    }
    
    private static void validateLocalUserData(String name, Email email, String passwordHash) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserDataException("Nome é obrigatório");
        }
        if (email == null) {
            throw new InvalidUserDataException("Email é obrigatório");
        }
        if (passwordHash == null || passwordHash.isEmpty()) {
            throw new InvalidUserDataException("Password hash é obrigatório");
        }
    }
    
    private static void validateOAuthUserData(String name, Email email, AuthProvider provider, String providerId) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserDataException("Nome é obrigatório");
        }
        if (email == null) {
            throw new InvalidUserDataException("Email é obrigatório");
        }
        if (provider == null || provider == AuthProvider.LOCAL) {
            throw new InvalidUserDataException("Provider OAuth é obrigatório");
        }
        if (providerId == null || providerId.isEmpty()) {
            throw new InvalidUserDataException("Provider ID é obrigatório");
        }
    }
    
    // Getters
    public UserId getId() { return id; }
    public String getName() { return name; }
    public Email getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public AuthProvider getProvider() { return provider; }
    public String getProviderId() { return providerId; }
    public boolean isEmailVerified() { return emailVerified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Equals e HashCode baseados no ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email=" + email +
                ", provider=" + provider +
                ", emailVerified=" + emailVerified +
                '}';
    }
}

// AuthProvider enum está em arquivo separado 