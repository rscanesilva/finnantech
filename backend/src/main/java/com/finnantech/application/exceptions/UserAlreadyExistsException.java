package com.finnantech.application.exceptions;

/**
 * Exceção lançada quando tenta-se cadastrar um usuário que já existe
 */
public class UserAlreadyExistsException extends ApplicationException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 