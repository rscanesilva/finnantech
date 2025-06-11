package com.finnantech.application.exceptions;

/**
 * Exceção base para todas as exceções da camada de aplicação
 */
public abstract class ApplicationException extends RuntimeException {
    
    public ApplicationException(String message) {
        super(message);
    }
    
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
} 