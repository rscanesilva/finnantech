package com.finnantech.domain.exceptions;

/**
 * Exceção base para todas as exceções do domínio
 */
public abstract class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
} 