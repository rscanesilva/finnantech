package com.finnantech.application.exceptions;

/**
 * Exceção lançada quando credenciais são inválidas
 */
public class InvalidCredentialsException extends ApplicationException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
} 