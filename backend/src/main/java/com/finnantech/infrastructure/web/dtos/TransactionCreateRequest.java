package com.finnantech.infrastructure.web.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para criação de transações
 */
public class TransactionCreateRequest {
    
    @NotBlank(message = "ID da categoria é obrigatório")
    private String categoryId;
    
    private String paymentMethodId;
    
    @NotBlank(message = "Tipo é obrigatório")
    @Pattern(regexp = "^(RECEITA|DESPESA)$", message = "Tipo deve ser RECEITA ou DESPESA")
    private String type;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal amount;
    
    @NotBlank(message = "Moeda é obrigatória")
    @Size(min = 3, max = 3, message = "Moeda deve ter 3 caracteres")
    private String currency;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String description;
    
    @Size(max = 1000, message = "Notas devem ter no máximo 1000 caracteres")
    private String notes;
    
    @NotNull(message = "Data da transação é obrigatória")
    private LocalDate transactionDate;
    
    @Size(max = 150, message = "Nome do estabelecimento deve ter no máximo 150 caracteres")
    private String merchantName;
    
    @Size(max = 100, message = "Categoria do estabelecimento deve ter no máximo 100 caracteres")
    private String merchantCategory;
    
    private List<String> tags;
    
    private Boolean isRecurring = false;

    // Construtores
    public TransactionCreateRequest() {}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }
} 