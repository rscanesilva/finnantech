package com.finnantech.domain.ports;

/**
 * Port para serviços relacionados a senhas
 */
public interface PasswordServicePort {
    
    /**
     * Gera hash da senha
     */
    String hashPassword(String rawPassword);
    
    /**
     * Verifica se a senha raw corresponde ao hash
     */
    boolean verifyPassword(String rawPassword, String hashedPassword);
    
    /**
     * Gera uma senha temporária
     */
    String generateTemporaryPassword();
} 