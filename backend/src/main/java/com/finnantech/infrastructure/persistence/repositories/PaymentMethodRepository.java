package com.finnantech.infrastructure.persistence.repositories;

import com.finnantech.domain.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para entidade PaymentMethod
 * Contém queries otimizadas para gestão de métodos de pagamento
 */
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    
    /**
     * Busca todos os métodos de pagamento ativos de um usuário
     */
    List<PaymentMethod> findByUserIdAndActiveTrueOrderByIsDefaultDescNameAsc(String userId);
    
    /**
     * Busca o método de pagamento padrão de um usuário
     */
    Optional<PaymentMethod> findByUserIdAndIsDefaultTrueAndActiveTrue(String userId);
    
    /**
     * Busca métodos de pagamento por tipo para um usuário
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.type = :type AND pm.active = true " +
           "ORDER BY pm.isDefault DESC, pm.name ASC")
    List<PaymentMethod> findByUserAndType(@Param("userId") String userId, 
                                         @Param("type") String type);
    
    /**
     * Busca todos os cartões de crédito de um usuário
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.type = 'CARTAO_CREDITO' AND pm.active = true " +
           "ORDER BY pm.isDefault DESC, pm.name ASC")
    List<PaymentMethod> findCreditCardsByUser(@Param("userId") String userId);
    
    /**
     * Busca todos os cartões de débito de um usuário
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.type = 'CARTAO_DEBITO' AND pm.active = true " +
           "ORDER BY pm.isDefault DESC, pm.name ASC")
    List<PaymentMethod> findDebitCardsByUser(@Param("userId") String userId);
    
    /**
     * Busca todos os cartões (crédito + débito) de um usuário
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.type IN ('CARTAO_CREDITO', 'CARTAO_DEBITO') AND pm.active = true " +
           "ORDER BY pm.type ASC, pm.isDefault DESC, pm.name ASC")
    List<PaymentMethod> findAllCardsByUser(@Param("userId") String userId);
    
    /**
     * Busca métodos não-cartão (PIX, dinheiro, etc.) de um usuário
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.type NOT IN ('CARTAO_CREDITO', 'CARTAO_DEBITO') AND pm.active = true " +
           "ORDER BY pm.isDefault DESC, pm.name ASC")
    List<PaymentMethod> findNonCardMethodsByUser(@Param("userId") String userId);
    
    /**
     * Conta quantos métodos de pagamento um usuário possui
     */
    long countByUserIdAndActiveTrue(String userId);
    
    /**
     * Conta quantos cartões de crédito um usuário possui
     */
    @Query("SELECT COUNT(pm) FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.type = 'CARTAO_CREDITO' AND pm.active = true")
    long countCreditCardsByUser(@Param("userId") String userId);
    
    /**
     * Verifica se existe método padrão para o usuário
     */
    boolean existsByUserIdAndIsDefaultTrueAndActiveTrue(String userId);
    
    /**
     * Busca por últimos dígitos do cartão
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.cardLastDigits = :lastDigits AND pm.active = true")
    List<PaymentMethod> findByUserAndLastDigits(@Param("userId") String userId, 
                                               @Param("lastDigits") String lastDigits);
    
    /**
     * Busca métodos mais utilizados por um usuário (com base em transações)
     */
    @Query("SELECT pm FROM PaymentMethod pm " +
           "JOIN Transaction t ON t.paymentMethodId = pm.id " +
           "WHERE pm.userId = :userId AND pm.active = true " +
           "GROUP BY pm.id " +
           "ORDER BY COUNT(t.id) DESC")
    List<PaymentMethod> findMostUsedByUser(@Param("userId") String userId);
    
    /**
     * Busca cartões que vencem em um determinado período
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.type IN ('CARTAO_CREDITO', 'CARTAO_DEBITO') " +
           "AND pm.cardExpiry LIKE :yearMonth% AND pm.active = true " +
           "ORDER BY pm.cardExpiry ASC")
    List<PaymentMethod> findCardsExpiringIn(@Param("userId") String userId, 
                                           @Param("yearMonth") String yearMonth);
    
    /**
     * Busca cartões por bandeira
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
           "AND pm.cardBrand = :brand AND pm.active = true " +
           "ORDER BY pm.isDefault DESC, pm.name ASC")
    List<PaymentMethod> findByUserAndBrand(@Param("userId") String userId, 
                                          @Param("brand") String brand);
} 