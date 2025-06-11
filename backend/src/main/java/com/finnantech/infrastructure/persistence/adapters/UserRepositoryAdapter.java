package com.finnantech.infrastructure.persistence.adapters;

import com.finnantech.domain.entities.User;
import com.finnantech.domain.ports.UserRepositoryPort;
import com.finnantech.domain.valueobjects.Email;
import com.finnantech.domain.valueobjects.UserId;
import com.finnantech.infrastructure.persistence.entities.UserEntity;
import com.finnantech.infrastructure.persistence.mappers.UserMapper;
import com.finnantech.infrastructure.persistence.repositories.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter que implementa UserRepositoryPort usando JPA
 * Esta classe conecta o domínio com a infraestrutura de persistência
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;
    
    public UserRepositoryAdapter(UserJpaRepository jpaRepository, UserMapper userMapper) {
        this.jpaRepository = jpaRepository;
        this.userMapper = userMapper;
    }
    
    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<User> findById(UserId userId) {
        Optional<UserEntity> entity = jpaRepository.findById(userId.getValue());
        return entity.map(userMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(Email email) {
        Optional<UserEntity> entity = jpaRepository.findByEmail(email.getValue());
        return entity.map(userMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
        Optional<UserEntity> entity = jpaRepository.findByProviderAndProviderId(provider, providerId);
        return entity.map(userMapper::toDomain);
    }
    
    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }
    
    @Override
    public void delete(User user) {
        UserEntity entity = userMapper.toEntity(user);
        jpaRepository.delete(entity);
    }
} 