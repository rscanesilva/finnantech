package com.finnantech.infrastructure.persistence.repositories;

import com.finnantech.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório JPA para operações de banco de dados do usuário
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    
    /**
     * Busca usuário por email
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * Busca usuário por provider e provider ID
     */
    @Query("SELECT u FROM UserEntity u WHERE u.provider = :provider AND u.providerId = :providerId")
    Optional<UserEntity> findByProviderAndProviderId(@Param("provider") String provider, 
                                                     @Param("providerId") String providerId);
    
    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica se existe usuário com provider e provider ID
     */
    boolean existsByProviderAndProviderId(String provider, String providerId);
} 