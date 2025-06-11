package com.finnantech.infrastructure.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de request para criação de Category
 * Contém validações para garantir dados corretos
 */
public class CategoryCreateRequest {
    
    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String name;
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Cor deve estar no formato hexadecimal (#RRGGBB)")
    private String color;
    
    @Size(max = 50, message = "Ícone deve ter no máximo 50 caracteres")
    private String icon;
    
    @NotBlank(message = "Tipo da categoria é obrigatório")
    @Pattern(regexp = "^(RECEITA|DESPESA|INVESTIMENTO)$", 
             message = "Tipo deve ser RECEITA, DESPESA ou INVESTIMENTO")
    private String type;
    
    // Construtores
    public CategoryCreateRequest() {}
    
    public CategoryCreateRequest(String name, String description, String color, String icon, String type) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.icon = icon;
        this.type = type;
    }
    
    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
} 