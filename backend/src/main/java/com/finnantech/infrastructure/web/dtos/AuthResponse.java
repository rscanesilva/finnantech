package com.finnantech.infrastructure.web.dtos;

/**
 * DTO para resposta de autenticação (login/register)
 */
public record AuthResponse(
        String token,
        String userId,
        String name,
        String email,
        boolean emailVerified,
        String message
) {
    public static AuthResponse success(String token, String userId, String name, 
                                     String email, boolean emailVerified, String message) {
        return new AuthResponse(token, userId, name, email, emailVerified, message);
    }
    
    public static AuthResponse error(String message) {
        return new AuthResponse(null, null, null, null, false, message);
    }
} 