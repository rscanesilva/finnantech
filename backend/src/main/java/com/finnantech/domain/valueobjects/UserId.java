package com.finnantech.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representando o ID único de um usuário
 */
public class UserId {
    
    private final String value;
    
    private UserId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("UserId não pode ser nulo ou vazio");
        }
        this.value = value.trim();
    }
    
    public static UserId of(String value) {
        return new UserId(value);
    }
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
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