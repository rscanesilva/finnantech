package com.finnantech.domain.ports;

import com.finnantech.domain.entities.User;

/**
 * Port para serviços relacionados a JWT
 */
public interface JwtServicePort {
    
    /**
     * Gera token JWT para o usuário
     */
    String generateToken(User user);
    
    /**
     * Extrai o email do token
     */
    String extractEmail(String token);
    
    /**
     * Verifica se o token é válido
     */
    boolean isTokenValid(String token, String email);
    
    /**
     * Verifica se o token expirou
     */
    boolean isTokenExpired(String token);
} 