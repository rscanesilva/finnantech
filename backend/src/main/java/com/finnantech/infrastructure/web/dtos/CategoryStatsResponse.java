package com.finnantech.infrastructure.web.dtos;

/**
 * DTO de resposta para estatísticas de categorias
 * Usado para exibir informações sobre uso de categorias
 */
public class CategoryStatsResponse {
    
    private Long customCategoriesCount;
    private Boolean canCreateMore;
    private Long maxCategories;
    
    // Construtores
    public CategoryStatsResponse() {}
    
    public CategoryStatsResponse(Long customCategoriesCount, Boolean canCreateMore, Long maxCategories) {
        this.customCategoriesCount = customCategoriesCount;
        this.canCreateMore = canCreateMore;
        this.maxCategories = maxCategories;
    }
    
    // Getters e Setters
    public Long getCustomCategoriesCount() { return customCategoriesCount; }
    public void setCustomCategoriesCount(Long customCategoriesCount) { 
        this.customCategoriesCount = customCategoriesCount; 
    }
    
    public Boolean getCanCreateMore() { return canCreateMore; }
    public void setCanCreateMore(Boolean canCreateMore) { this.canCreateMore = canCreateMore; }
    
    public Long getMaxCategories() { return maxCategories; }
    public void setMaxCategories(Long maxCategories) { this.maxCategories = maxCategories; }
} 