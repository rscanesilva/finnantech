package com.finnantech.domain.exceptions;

/**
 * Exceção lançada quando dados do usuário são inválidos
 */
public class InvalidUserDataException extends DomainException {
    
    public InvalidUserDataException(String message) {
        super(message);
    }
    
    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
} 