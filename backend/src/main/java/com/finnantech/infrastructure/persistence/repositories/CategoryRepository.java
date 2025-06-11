package com.finnantech.infrastructure.persistence.repositories;

import com.finnantech.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para entidade Category
 * Contém queries otimizadas para consultas financeiras
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    
    /**
     * Busca todas as categorias ativas de um usuário específico
     */
    List<Category> findByUserIdAndActiveTrue(String userId);
    
    /**
     * Busca todas as categorias do sistema (padrão) ativas
     */
    List<Category> findByIsSystemDefaultTrueAndActiveTrue();
    
    /**
     * Busca categorias por tipo para um usuário específico
     */
    @Query("SELECT c FROM Category c WHERE (c.userId = :userId OR c.isSystemDefault = true) " +
           "AND c.type = :type AND c.active = true " +
           "ORDER BY c.isSystemDefault DESC, c.name ASC")
    List<Category> findByUserAndType(@Param("userId") String userId, 
                                    @Param("type") String type);
    
    /**
     * Busca todas as categorias disponíveis para um usuário (próprias + sistema)
     */
    @Query("SELECT c FROM Category c WHERE (c.userId = :userId OR c.isSystemDefault = true) " +
           "AND c.active = true " +
           "ORDER BY c.isSystemDefault DESC, c.type ASC, c.name ASC")
    List<Category> findAllAvailableForUser(@Param("userId") String userId);
    
    /**
     * Busca categoria por nome e usuário (evita duplicação)
     */
    Optional<Category> findByUserIdAndNameAndActiveTrue(String userId, String name);
    
    /**
     * Busca categorias de despesa para um usuário
     */
    @Query("SELECT c FROM Category c WHERE (c.userId = :userId OR c.isSystemDefault = true) " +
           "AND c.type = 'DESPESA' AND c.active = true " +
           "ORDER BY c.isSystemDefault DESC, c.name ASC")
    List<Category> findExpenseCategoriesForUser(@Param("userId") String userId);
    
    /**
     * Busca categorias de receita para um usuário
     */
    @Query("SELECT c FROM Category c WHERE (c.userId = :userId OR c.isSystemDefault = true) " +
           "AND c.type = 'RECEITA' AND c.active = true " +
           "ORDER BY c.isSystemDefault DESC, c.name ASC")
    List<Category> findIncomeCategoriesForUser(@Param("userId") String userId);
    
    /**
     * Busca categorias de investimento para um usuário
     */
    @Query("SELECT c FROM Category c WHERE (c.userId = :userId OR c.isSystemDefault = true) " +
           "AND c.type = 'INVESTIMENTO' AND c.active = true " +
           "ORDER BY c.isSystemDefault DESC, c.name ASC")
    List<Category> findInvestmentCategoriesForUser(@Param("userId") String userId);
    
    /**
     * Conta quantas categorias personalizadas um usuário possui
     */
    @Query("SELECT COUNT(c) FROM Category c WHERE c.userId = :userId AND c.active = true")
    long countUserCategories(@Param("userId") String userId);
    
    /**
     * Verifica se existe categoria com o mesmo nome para o usuário
     */
    boolean existsByUserIdAndNameAndActiveTrue(String userId, String name);
    
    /**
     * Busca categorias mais utilizadas por um usuário (com base em transações)
     */
    @Query("SELECT c FROM Category c " +
           "JOIN Transaction t ON t.categoryId = c.id " +
           "WHERE t.userId = :userId AND c.active = true " +
           "GROUP BY c.id " +
           "ORDER BY COUNT(t.id) DESC")
    List<Category> findMostUsedCategoriesByUser(@Param("userId") String userId);
    
    /**
     * Busca categorias por cor específica
     */
    List<Category> findByColorAndActiveTrue(String color);
} 