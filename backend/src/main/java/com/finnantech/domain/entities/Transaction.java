package com.finnantech.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Entidade Transaction do domínio
 * Core do sistema financeiro - representa todas as movimentações
 * Otimizada para consultas de dashboard e relatórios
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transactions_user_date", columnList = "user_id, transaction_date DESC"),
    @Index(name = "idx_transactions_user_month", columnList = "user_id, month_year DESC"),
    @Index(name = "idx_transactions_category", columnList = "category_id"),
    @Index(name = "idx_transactions_payment_method", columnList = "payment_method_id"),
    @Index(name = "idx_transactions_type_date", columnList = "type, transaction_date DESC"),
    @Index(name = "idx_transactions_merchant", columnList = "merchant_name"),
    @Index(name = "idx_transactions_recurring", columnList = "user_id, is_recurring"),
    @Index(name = "idx_transactions_status", columnList = "status"),
    @Index(name = "idx_transactions_user_type_date", columnList = "user_id, type, transaction_date DESC")
})
public class Transaction {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @NotBlank(message = "ID do usuário é obrigatório")
    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;
    
    @NotBlank(message = "ID da categoria é obrigatório")
    @Column(name = "category_id", length = 36, nullable = false)
    private String categoryId;
    
    @Column(name = "payment_method_id", length = 36)
    private String paymentMethodId; // Pode ser NULL para transações importadas
    
    @NotNull(message = "Tipo da transação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;
    
    @NotNull(message = "Valor da transação é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2, message = "Valor deve ter no máximo 13 dígitos inteiros e 2 decimais")
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;
    
    @NotBlank(message = "Moeda é obrigatória")
    @Pattern(regexp = "^(BRL|USD|EUR)$", message = "Moeda deve ser BRL, USD ou EUR")
    @Column(name = "currency", length = 3, nullable = false)
    private String currency = "BRL";
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    @Column(name = "description", length = 255, nullable = false)
    private String description;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @NotNull(message = "Data da transação é obrigatória")
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Dados do estabelecimento/origem
    @Size(max = 150, message = "Nome do estabelecimento deve ter no máximo 150 caracteres")
    @Column(name = "merchant_name", length = 150)
    private String merchantName;
    
    @Size(max = 100, message = "Categoria do estabelecimento deve ter no máximo 100 caracteres")
    @Column(name = "merchant_category", length = 100)
    private String merchantCategory;
    
    // Metadados importantes
    @NotNull(message = "Status da transação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.CONFIRMADA;
    
    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring = false;
    
    @Column(name = "recurring_group_id", length = 36)
    private String recurringGroupId;
    
    @Size(max = 255, message = "ID externo deve ter no máximo 255 caracteres")
    @Column(name = "external_id", length = 255)
    private String externalId;
    
    // Tags para organização (JSON)
    @Column(name = "tags_json", columnDefinition = "TEXT")
    private String tagsJson;
    
    // Campo calculado para performance
    @Column(name = "month_year", length = 7)
    private String monthYear; // YYYY-MM gerado automaticamente
    
    // Construtores
    public Transaction() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Transaction(String userId, String categoryId, TransactionType type, 
                      BigDecimal amount, String description, LocalDate transactionDate) {
        this();
        this.userId = userId;
        this.categoryId = categoryId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.generateMonthYear();
    }
    
    // Métodos de negócio
    public boolean isIncome() {
        return type == TransactionType.RECEITA;
    }
    
    public boolean isExpense() {
        return type == TransactionType.DESPESA;
    }
    
    public boolean isPending() {
        return status == TransactionStatus.PENDENTE;
    }
    
    public boolean isConfirmed() {
        return status == TransactionStatus.CONFIRMADA;
    }
    
    public boolean isCancelled() {
        return status == TransactionStatus.CANCELADA;
    }
    
    public void confirm() {
        this.status = TransactionStatus.CONFIRMADA;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        this.status = TransactionStatus.CANCELADA;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateBasicInfo(String description, BigDecimal amount, LocalDate transactionDate, String notes) {
        if (description != null && !description.trim().isEmpty()) {
            this.description = description.trim();
        }
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.amount = amount;
        }
        if (transactionDate != null) {
            this.transactionDate = transactionDate;
            this.generateMonthYear();
        }
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateMerchantInfo(String merchantName, String merchantCategory) {
        this.merchantName = merchantName;
        this.merchantCategory = merchantCategory;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setAsRecurring(String recurringGroupId) {
        this.isRecurring = true;
        this.recurringGroupId = recurringGroupId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeFromRecurring() {
        this.isRecurring = false;
        this.recurringGroupId = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Geração automática do campo month_year
    private void generateMonthYear() {
        if (transactionDate != null) {
            this.monthYear = transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
    }
    
    // Helper para tags
    public void setTags(List<String> tags) {
        if (tags != null && !tags.isEmpty()) {
            // Converte lista para JSON string simples
            this.tagsJson = "[\"" + String.join("\", \"", tags) + "\"]";
        } else {
            this.tagsJson = null;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    // JPA Callbacks
    @PrePersist
    protected void onCreate() {
        this.generateMonthYear();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.generateMonthYear();
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    
    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { 
        this.transactionDate = transactionDate;
        this.generateMonthYear();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    
    public String getMerchantCategory() { return merchantCategory; }
    public void setMerchantCategory(String merchantCategory) { this.merchantCategory = merchantCategory; }
    
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    
    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }
    
    public String getRecurringGroupId() { return recurringGroupId; }
    public void setRecurringGroupId(String recurringGroupId) { this.recurringGroupId = recurringGroupId; }
    
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    
    public String getTagsJson() { return tagsJson; }
    public void setTagsJson(String tagsJson) { this.tagsJson = tagsJson; }
    
    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }
    
    // Equals e HashCode baseados no ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", transactionDate=" + transactionDate +
                ", status=" + status +
                '}';
    }
}

/**
 * Enum para tipos de transação
 */
enum TransactionType {
    RECEITA,
    DESPESA
}

/**
 * Enum para status de transação
 */
enum TransactionStatus {
    PENDENTE,
    CONFIRMADA,
    CANCELADA
} 