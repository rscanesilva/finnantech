package com.finnantech.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representando um endereço de email válido
 */
public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private final String value;
    
    private Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        
        String normalizedEmail = value.trim().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Email possui formato inválido: " + value);
        }
        
        this.value = normalizedEmail;
    }
    
    public static Email of(String value) {
        return new Email(value);
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }
    
    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
} 