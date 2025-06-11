package com.finnantech.infrastructure.web.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para atualização de transações
 */
public class TransactionUpdateRequest {
    
    private String categoryId;
    private String paymentMethodId;
    
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal amount;
    
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String description;
    
    @Size(max = 1000, message = "Notas devem ter no máximo 1000 caracteres")
    private String notes;
    
    private LocalDate transactionDate;
    
    @Size(max = 150, message = "Nome do estabelecimento deve ter no máximo 150 caracteres")
    private String merchantName;
    
    @Size(max = 100, message = "Categoria do estabelecimento deve ter no máximo 100 caracteres")
    private String merchantCategory;
    
    private List<String> tags;

    // Construtores
    public TransactionUpdateRequest() {}

    // Getters e Setters
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
} 