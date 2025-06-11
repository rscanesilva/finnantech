package com.finnantech.application.services;

import com.finnantech.domain.entities.Category;
import com.finnantech.domain.entities.CategoryType;
import com.finnantech.infrastructure.persistence.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service para gerenciamento de categorias
 * Contém todas as regras de negócio relacionadas a categorias
 */
@Service
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    /**
     * Busca todas as categorias disponíveis para um usuário
     * (categorias próprias + categorias padrão do sistema)
     */
    public List<Category> getAllCategoriesForUser(String userId) {
        return categoryRepository.findAllAvailableForUser(userId);
    }
    
    /**
     * Busca categorias por tipo para um usuário
     */
    public List<Category> getCategoriesByType(String userId, String type) {
        return categoryRepository.findByUserAndType(userId, type);
    }
    
    /**
     * Busca categorias de despesa para um usuário
     */
    public List<Category> getExpenseCategories(String userId) {
        return categoryRepository.findExpenseCategoriesForUser(userId);
    }
    
    /**
     * Busca categorias de receita para um usuário
     */
    public List<Category> getIncomeCategories(String userId) {
        return categoryRepository.findIncomeCategoriesForUser(userId);
    }
    
    /**
     * Busca categorias de investimento para um usuário
     */
    public List<Category> getInvestmentCategories(String userId) {
        return categoryRepository.findInvestmentCategoriesForUser(userId);
    }
    
    /**
     * Busca categoria por ID
     */
    public Optional<Category> getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId);
    }
    
    /**
     * Cria uma nova categoria personalizada para um usuário
     */
    public Category createUserCategory(String userId, String name, String description, 
                                     String color, String icon, String type) {
        
        // Validar se já existe categoria com esse nome para o usuário
        if (categoryRepository.existsByUserIdAndNameAndActiveTrue(userId, name)) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }
        
        // Validar limite de categorias personalizadas (exemplo: máximo 50)
        long userCategoriesCount = categoryRepository.countUserCategories(userId);
        if (userCategoriesCount >= 50) {
            throw new IllegalArgumentException("Limite máximo de categorias personalizadas atingido (50)");
        }
        
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setUserId(userId);
        category.setName(name);
        category.setDescription(description);
        category.setColor(color != null ? color : "#5392ff");
        category.setIcon(icon != null ? icon : "ShoppingCart");
        
        // Validar e definir o tipo
        try {
            // Assumindo que CategoryType é um enum
            category.setType(CategoryType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de categoria inválido: " + type);
        }
        
        category.setIsSystemDefault(false);
        category.setActive(true);
        
        return categoryRepository.save(category);
    }
    
    /**
     * Atualiza uma categoria personalizada do usuário
     */
    public Category updateUserCategory(String userId, String categoryId, String name, 
                                     String description, String color, String icon) {
        
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        // Verificar se a categoria pertence ao usuário
        if (!userId.equals(category.getUserId())) {
            throw new IllegalArgumentException("Usuário não tem permissão para editar esta categoria");
        }
        
        // Não permitir edição de categorias padrão do sistema
        if (category.getIsSystemDefault()) {
            throw new IllegalArgumentException("Não é possível editar categorias padrão do sistema");
        }
        
        // Verificar se novo nome já existe (se foi alterado)
        if (!category.getName().equals(name) && 
            categoryRepository.existsByUserIdAndNameAndActiveTrue(userId, name)) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }
        
        category.updateInfo(name, description, color, icon);
        
        return categoryRepository.save(category);
    }
    
    /**
     * Desativa uma categoria personalizada do usuário
     */
    public void deactivateUserCategory(String userId, String categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        // Verificar se a categoria pertence ao usuário
        if (!userId.equals(category.getUserId())) {
            throw new IllegalArgumentException("Usuário não tem permissão para desativar esta categoria");
        }
        
        // Não permitir desativação de categorias padrão do sistema
        if (category.getIsSystemDefault()) {
            throw new IllegalArgumentException("Não é possível desativar categorias padrão do sistema");
        }
        
        // TODO: Verificar se existem transações ativas usando esta categoria
        // Se existirem, talvez deveria mover para uma categoria padrão
        
        category.deactivate();
        categoryRepository.save(category);
    }
    
    /**
     * Reativa uma categoria personalizada do usuário
     */
    public void reactivateUserCategory(String userId, String categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        // Verificar se a categoria pertence ao usuário
        if (!userId.equals(category.getUserId())) {
            throw new IllegalArgumentException("Usuário não tem permissão para reativar esta categoria");
        }
        
        category.activate();
        categoryRepository.save(category);
    }
    
    /**
     * Busca categorias mais utilizadas pelo usuário
     */
    public List<Category> getMostUsedCategories(String userId) {
        return categoryRepository.findMostUsedCategoriesByUser(userId);
    }
    
    /**
     * Busca apenas categorias personalizadas do usuário
     */
    public List<Category> getUserCustomCategories(String userId) {
        return categoryRepository.findByUserIdAndActiveTrue(userId);
    }
    
    /**
     * Busca apenas categorias padrão do sistema
     */
    public List<Category> getSystemCategories() {
        return categoryRepository.findByIsSystemDefaultTrueAndActiveTrue();
    }
    
    /**
     * Conta o número de categorias personalizadas do usuário
     */
    public long countUserCategories(String userId) {
        return categoryRepository.countUserCategories(userId);
    }
    
    /**
     * Verifica se o usuário pode criar mais categorias
     */
    public boolean canCreateMoreCategories(String userId) {
        return countUserCategories(userId) < 50; // Limite configurável
    }
    
    /**
     * Valida se uma categoria pode ser usada pelo usuário
     */
    public boolean canUserUseCategory(String userId, String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        
        if (category.isEmpty() || !category.get().getActive()) {
            return false;
        }
        
        Category cat = category.get();
        
        // Pode usar se for categoria própria ou categoria padrão do sistema
        return userId.equals(cat.getUserId()) || cat.getIsSystemDefault();
    }
} 