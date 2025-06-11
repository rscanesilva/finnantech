package com.finnantech.infrastructure.persistence.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finnantech.domain.entities.Transaction;

/**
 * Repository para entidade Transaction
 * Contém queries otimizadas para dashboard e relatórios financeiros
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    /**
     * Busca transações de um usuário com paginação
     */
    Page<Transaction> findByUserIdOrderByTransactionDateDescCreatedAtDesc(String userId, Pageable pageable);
    
    /**
     * Busca transações de um usuário em um período específico
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.transactionDate DESC, t.createdAt DESC")
    List<Transaction> findByUserAndDateRange(@Param("userId") String userId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
    
    /**
     * Busca transações por categoria
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.categoryId = :categoryId AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserAndCategory(@Param("userId") String userId,
                                          @Param("categoryId") String categoryId);
    
    /**
     * Busca transações por método de pagamento
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.paymentMethodId = :paymentMethodId AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserAndPaymentMethod(@Param("userId") String userId,
                                                @Param("paymentMethodId") String paymentMethodId);
    
    /**
     * Busca transações por tipo (RECEITA ou DESPESA)
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.type = :type AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserAndType(@Param("userId") String userId,
                                       @Param("type") String type);
    
    /**
     * DASHBOARD: Soma total de receitas de um usuário em período
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId " +
           "AND t.type = 'RECEITA' AND t.status = 'CONFIRMADA' " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalIncomeByUserAndPeriod(@Param("userId") String userId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
    
    /**
     * DASHBOARD: Soma total de despesas de um usuário em período
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId " +
           "AND t.type = 'DESPESA' AND t.status = 'CONFIRMADA' " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpensesByUserAndPeriod(@Param("userId") String userId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);
    
    /**
     * DASHBOARD: Últimas transações de um usuário
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.transactionDate DESC, t.createdAt DESC " +
           "LIMIT :limit")
    List<Transaction> findLatestTransactionsByUser(@Param("userId") String userId,
                                                  @Param("limit") int limit);
    
    /**
     * DASHBOARD: Gastos por categoria em período
     */
    @Query("SELECT t.categoryId, SUM(t.amount) FROM Transaction t WHERE t.userId = :userId " +
           "AND t.type = 'DESPESA' AND t.status = 'CONFIRMADA' " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY t.categoryId " +
           "ORDER BY SUM(t.amount) DESC")
    List<Object[]> getExpensesByCategoryAndPeriod(@Param("userId") String userId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
    
    /**
     * DASHBOARD: Despesas mensais para gráfico de barras
     */
    @Query(value = "SELECT YEAR(transaction_date) as ano, MONTH(transaction_date) as mes, SUM(amount) as total " +
           "FROM transactions WHERE user_id = ?1 " +
           "AND status = 'CONFIRMADA' " +
           "AND type = 'DESPESA' " +
           "AND transaction_date >= ?2 " +
           "AND transaction_date <= ?3 " +
           "GROUP BY YEAR(transaction_date), MONTH(transaction_date) " +
           "ORDER BY YEAR(transaction_date) ASC, MONTH(transaction_date) ASC", 
           nativeQuery = true)
    List<Object[]> getMonthlyExpenses(@Param("userId") String userId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);
    
    /**
     * Busca transações pendentes de um usuário
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.status = 'PENDENTE' " +
           "ORDER BY t.transactionDate ASC")
    List<Transaction> findPendingTransactionsByUser(@Param("userId") String userId);
    
    /**
     * Busca transações recorrentes de um usuário
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.isRecurring = true AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findRecurringTransactionsByUser(@Param("userId") String userId);
    
    /**
     * Busca transações por grupo recorrente
     */
    @Query("SELECT t FROM Transaction t WHERE t.recurringGroupId = :groupId " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByRecurringGroupId(@Param("groupId") String groupId);
    
    /**
     * Conta transações por status
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.userId = :userId AND t.status = :status")
    long countByUserAndStatus(@Param("userId") String userId, @Param("status") String status);
    
    /**
     * Busca transações por estabelecimento
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.merchantName = :merchantName AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserAndMerchant(@Param("userId") String userId,
                                          @Param("merchantName") String merchantName);
    
    /**
     * Busca transações com valor acima de um limite
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.amount >= :minAmount AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.amount DESC, t.transactionDate DESC")
    List<Transaction> findByUserAndMinAmount(@Param("userId") String userId,
                                           @Param("minAmount") BigDecimal minAmount);
    
    /**
     * RELATÓRIOS: Soma por método de pagamento em período
     */
    @Query("SELECT t.paymentMethodId, SUM(t.amount) FROM Transaction t WHERE t.userId = :userId " +
           "AND t.status = 'CONFIRMADA' " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY t.paymentMethodId " +
           "ORDER BY SUM(t.amount) DESC")
    List<Object[]> getSumByPaymentMethodAndPeriod(@Param("userId") String userId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
    
    /**
     * RELATÓRIOS: Média de gastos por categoria
     */
    @Query("SELECT t.categoryId, AVG(t.amount) FROM Transaction t WHERE t.userId = :userId " +
           "AND t.type = 'DESPESA' AND t.status = 'CONFIRMADA' " +
           "GROUP BY t.categoryId " +
           "HAVING COUNT(t.id) >= :minTransactions " +
           "ORDER BY AVG(t.amount) DESC")
    List<Object[]> getAverageExpensesByCategory(@Param("userId") String userId,
                                              @Param("minTransactions") int minTransactions);
    
    /**
     * Busca duplicatas potenciais (mesmo valor, data e usuário)
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.amount = :amount AND t.transactionDate = :date " +
           "AND t.status = 'CONFIRMADA' " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findPotentialDuplicates(@Param("userId") String userId,
                                            @Param("amount") BigDecimal amount,
                                            @Param("date") LocalDate date);
} 