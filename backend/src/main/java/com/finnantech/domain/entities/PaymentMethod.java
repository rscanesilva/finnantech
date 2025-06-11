package com.finnantech.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade PaymentMethod do domínio
 * Representa diferentes métodos de pagamento dos usuários
 * Suporta cartões de crédito/débito, PIX, dinheiro, transferências, etc.
 */
@Entity
@Table(name = "payment_methods", indexes = {
    @Index(name = "idx_payment_methods_user", columnList = "user_id"),
    @Index(name = "idx_payment_methods_type", columnList = "type"),
    @Index(name = "idx_payment_methods_default", columnList = "user_id, is_default")
})
public class PaymentMethod {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @NotBlank(message = "ID do usuário é obrigatório")
    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;
    
    @NotNull(message = "Tipo do método de pagamento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentMethodType type;
    
    @NotBlank(message = "Nome do método de pagamento é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    // Dados específicos para cartões
    @Size(min = 4, max = 4, message = "Últimos dígitos devem ter exatamente 4 caracteres")
    @Pattern(regexp = "\\d{4}", message = "Últimos dígitos devem ser numéricos")
    @Column(name = "card_last_digits", length = 4)
    private String cardLastDigits;
    
    @Size(max = 50, message = "Bandeira do cartão deve ter no máximo 50 caracteres")
    @Column(name = "card_brand", length = 50)
    private String cardBrand;
    
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$", message = "Validade deve estar no formato MM/YYYY")
    @Column(name = "card_expiry", length = 7)
    private String cardExpiry;
    
    @DecimalMin(value = "0.01", message = "Limite deve ser maior que zero")
    @Digits(integer = 13, fraction = 2, message = "Limite deve ter no máximo 13 dígitos inteiros e 2 decimais")
    @Column(name = "card_limit", precision = 15, scale = 2)
    private BigDecimal cardLimit;
    
    @Min(value = 1, message = "Dia de fechamento deve estar entre 1 e 31")
    @Max(value = 31, message = "Dia de fechamento deve estar entre 1 e 31")
    @Column(name = "card_closing_day")
    private Integer cardClosingDay;
    
    @Min(value = 1, message = "Dia de vencimento deve estar entre 1 e 31")
    @Max(value = 31, message = "Dia de vencimento deve estar entre 1 e 31")
    @Column(name = "card_due_day")
    private Integer cardDueDay;
    
    // Metadados
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Construtores
    public PaymentMethod() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public PaymentMethod(String userId, PaymentMethodType type, String name) {
        this();
        this.userId = userId;
        this.type = type;
        this.name = name;
    }
    
    // Métodos de negócio
    public boolean isCardType() {
        return type == PaymentMethodType.CARTAO_CREDITO || type == PaymentMethodType.CARTAO_DEBITO;
    }
    
    public boolean isCreditCard() {
        return type == PaymentMethodType.CARTAO_CREDITO;
    }
    
    public boolean isDebitCard() {
        return type == PaymentMethodType.CARTAO_DEBITO;
    }
    
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeAsDefault() {
        this.isDefault = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateCardInfo(String lastDigits, String brand, String expiry, BigDecimal limit, 
                              Integer closingDay, Integer dueDay) {
        if (isCardType()) {
            this.cardLastDigits = lastDigits;
            this.cardBrand = brand;
            this.cardExpiry = expiry;
            this.cardLimit = limit;
            this.cardClosingDay = closingDay;
            this.cardDueDay = dueDay;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void deactivate() {
        this.active = false;
        this.isDefault = false; // Se foi desativado, não pode ser padrão
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDisplayName() {
        if (isCardType() && cardLastDigits != null) {
            return name + " *" + cardLastDigits;
        }
        return name;
    }
    
    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void validate() {
        // Validação: campos de cartão só devem estar preenchidos para cartões
        if (!isCardType()) {
            this.cardLastDigits = null;
            this.cardBrand = null;
            this.cardExpiry = null;
            this.cardLimit = null;
            this.cardClosingDay = null;
            this.cardDueDay = null;
        }
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public PaymentMethodType getType() { return type; }
    public void setType(PaymentMethodType type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCardLastDigits() { return cardLastDigits; }
    public void setCardLastDigits(String cardLastDigits) { this.cardLastDigits = cardLastDigits; }
    
    public String getCardBrand() { return cardBrand; }
    public void setCardBrand(String cardBrand) { this.cardBrand = cardBrand; }
    
    public String getCardExpiry() { return cardExpiry; }
    public void setCardExpiry(String cardExpiry) { this.cardExpiry = cardExpiry; }
    
    public BigDecimal getCardLimit() { return cardLimit; }
    public void setCardLimit(BigDecimal cardLimit) { this.cardLimit = cardLimit; }
    
    public Integer getCardClosingDay() { return cardClosingDay; }
    public void setCardClosingDay(Integer cardClosingDay) { this.cardClosingDay = cardClosingDay; }
    
    public Integer getCardDueDay() { return cardDueDay; }
    public void setCardDueDay(Integer cardDueDay) { this.cardDueDay = cardDueDay; }
    
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
    
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
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", isDefault=" + isDefault +
                ", active=" + active +
                '}';
    }
}

/**
 * Enum para tipos de método de pagamento
 */
enum PaymentMethodType {
    CARTAO_CREDITO,
    CARTAO_DEBITO,
    PIX,
    DINHEIRO,
    TRANSFERENCIA,
    BOLETO
} 