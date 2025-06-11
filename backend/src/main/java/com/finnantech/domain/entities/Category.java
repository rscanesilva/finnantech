package com.finnantech.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade Category do domínio
 * Representa categorias de transações (receitas, despesas, investimentos)
 * Suporta categorias padrão do sistema e personalizadas dos usuários
 */
@Entity
@Table(name = "categories", indexes = {
    @Index(name = "idx_categories_user_active", columnList = "user_id, active"),
    @Index(name = "idx_categories_type", columnList = "type"),
    @Index(name = "idx_categories_system_default", columnList = "is_system_default")
})
public class Category {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @Column(name = "user_id", length = 36)
    private String userId; // NULL para categorias padrão do sistema
    
    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(max = 100, message = "Nome da categoria deve ter no máximo 100 caracteres")
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Cor deve estar no formato hexadecimal (#RRGGBB)")
    @Column(name = "color", length = 7, nullable = false)
    private String color = "#5392ff"; // Default color
    
    @Size(max = 50, message = "Ícone deve ter no máximo 50 caracteres")
    @Column(name = "icon", length = 50)
    private String icon = "ShoppingCart"; // Default icon
    
    @NotNull(message = "Tipo da categoria é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CategoryType type;
    
    @Column(name = "is_system_default", nullable = false)
    private Boolean isSystemDefault = false;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Construtores
    public Category() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Category(String name, CategoryType type, String userId) {
        this();
        this.name = name;
        this.type = type;
        this.userId = userId;
    }
    
    // Métodos de negócio
    public boolean isUserCategory() {
        return userId != null && !isSystemDefault;
    }
    
    public boolean isSystemCategory() {
        return userId == null && isSystemDefault;
    }
    
    public void updateInfo(String name, String description, String color, String icon) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
        this.description = description;
        if (color != null && color.matches("^#[0-9A-Fa-f]{6}$")) {
            this.color = color;
        }
        if (icon != null && !icon.trim().isEmpty()) {
            this.icon = icon.trim();
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public CategoryType getType() { return type; }
    public void setType(CategoryType type) { this.type = type; }
    
    public Boolean getIsSystemDefault() { return isSystemDefault; }
    public void setIsSystemDefault(Boolean isSystemDefault) { this.isSystemDefault = isSystemDefault; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Equals e HashCode baseados no ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                ", isSystemDefault=" + isSystemDefault +
                ", active=" + active +
                '}';
    }
} 