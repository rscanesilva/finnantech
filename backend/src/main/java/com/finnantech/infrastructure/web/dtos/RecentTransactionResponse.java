package com.finnantech.infrastructure.web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para resposta de transações recentes no dashboard
 */
public class RecentTransactionResponse {
    
    private String id;
    private String description;
    private BigDecimal amount;
    private String type; // RECEITA ou DESPESA
    private String categoryName;
    private String categoryIcon;
    private String paymentMethodName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    // Construtores
    public RecentTransactionResponse() {}

    public RecentTransactionResponse(String id, String description, BigDecimal amount, 
                                   String type, String categoryName, String categoryIcon,
                                   String paymentMethodName, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.paymentMethodName = paymentMethodName;
        this.date = date;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
} 