package com.finnantech.infrastructure.web.dtos;

import java.math.BigDecimal;

/**
 * DTO para estatísticas de categoria
 */
public class CategoryStatsResponse {
    
    private String categoryId;
    private String categoryName;
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private Double percentage;

    // Construtores
    public CategoryStatsResponse() {}

    public CategoryStatsResponse(String categoryId, String categoryName, BigDecimal totalAmount, 
                               Integer transactionCount, Double percentage) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
        this.percentage = percentage;
    }

    // Getters e Setters
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
} 