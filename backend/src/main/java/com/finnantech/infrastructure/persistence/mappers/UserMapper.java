package com.finnantech.infrastructure.persistence.mappers;

import com.finnantech.domain.entities.User;
import com.finnantech.domain.entities.AuthProvider;
import com.finnantech.domain.valueobjects.Email;
import com.finnantech.domain.valueobjects.UserId;
import com.finnantech.infrastructure.persistence.entities.UserEntity;
import com.finnantech.infrastructure.persistence.entities.AuthProviderEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversão entre entidades de domínio e infraestrutura
 */
@Component  
public class UserMapper {
    
    /**
     * Converte User (domínio) para UserEntity (infraestrutura)
     */
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserEntity(
            user.getId().getValue(),
            user.getName(),
            user.getEmail().getValue(),
            user.getPasswordHash(),
            mapToEntity(user.getProvider()),
            user.getProviderId(),
            user.isEmailVerified(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    /**
     * Converte UserEntity (infraestrutura) para User (domínio)
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return User.reconstruct(
            UserId.of(entity.getId()),
            entity.getName(),
            Email.of(entity.getEmail()),
            entity.getPasswordHash(),
            mapToDomain(entity.getProvider()),
            entity.getProviderId(),
            entity.getEmailVerified(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    /**
     * Mapeia AuthProvider (domínio) para AuthProviderEntity (infraestrutura)
     */
    private AuthProviderEntity mapToEntity(AuthProvider provider) {
        return switch (provider) {
            case LOCAL -> AuthProviderEntity.LOCAL;
            case GOOGLE -> AuthProviderEntity.GOOGLE;
            case FACEBOOK -> AuthProviderEntity.FACEBOOK;
        };
    }
    
    /**
     * Mapeia AuthProviderEntity (infraestrutura) para AuthProvider (domínio)
     */
    private AuthProvider mapToDomain(AuthProviderEntity provider) {
        return switch (provider) {
            case LOCAL -> AuthProvider.LOCAL;
            case GOOGLE -> AuthProvider.GOOGLE;
            case FACEBOOK -> AuthProvider.FACEBOOK;
        };
    }
} 