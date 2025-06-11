package com.finnantech.infrastructure.web.controllers;

import com.finnantech.application.services.CategoryService;
import com.finnantech.domain.entities.Category;
import com.finnantech.infrastructure.web.dtos.CategoryCreateRequest;
import com.finnantech.infrastructure.web.dtos.CategoryResponse;
import com.finnantech.infrastructure.web.dtos.CategoryUpdateRequest;
import com.finnantech.infrastructure.web.dtos.CategoryStatsResponse;
import com.finnantech.infrastructure.web.dtos.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para gerenciamento de categorias
 * Endpoints para CRUD de categorias e consultas especializadas
 */
@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
@Tag(name = "Categories", description = "Endpoints para gerenciamento de categorias")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    /**
     * Lista todas as categorias disponíveis para o usuário
     * (categorias próprias + categorias padrão do sistema)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(Authentication auth) {
        try {
            String userId = auth.getName(); // Obtém o ID do usuário do JWT
            List<Category> categories = categoryService.getAllCategoriesForUser(userId);
            
            List<CategoryResponse> response = categories.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(response, "Categorias carregadas com sucesso"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar categorias: " + e.getMessage()));
        }
    }
    
    /**
     * Lista categorias por tipo (RECEITA, DESPESA, INVESTIMENTO)
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoriesByType(
            @PathVariable String type, Authentication auth) {
        try {
            String userId = auth.getName();
            List<Category> categories = categoryService.getCategoriesByType(userId, type.toUpperCase());
            
            List<CategoryResponse> response = categories.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(response, 
                    "Categorias de " + type.toLowerCase() + " carregadas com sucesso"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erro ao carregar categorias por tipo: " + e.getMessage()));
        }
    }
    
    /**
     * Lista apenas categorias de despesa
     */
    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getExpenseCategories(Authentication auth) {
        try {
            String userId = auth.getName();
            List<Category> categories = categoryService.getExpenseCategories(userId);
            
            List<CategoryResponse> response = categories.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(response, "Categorias de despesa carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar categorias de despesa: " + e.getMessage()));
        }
    }
    
    /**
     * Lista apenas categorias de receita
     */
    @GetMapping("/income")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getIncomeCategories(Authentication auth) {
        try {
            String userId = auth.getName();
            List<Category> categories = categoryService.getIncomeCategories(userId);
            
            List<CategoryResponse> response = categories.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(response, "Categorias de receita carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar categorias de receita: " + e.getMessage()));
        }
    }
    
    /**
     * Lista apenas categorias de investimento
     */
    @GetMapping("/investments")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getInvestmentCategories(Authentication auth) {
        try {
            String userId = auth.getName();
            List<Category> categories = categoryService.getInvestmentCategories(userId);
            
            List<CategoryResponse> response = categories.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(response, "Categorias de investimento carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar categorias de investimento: " + e.getMessage()));
        }
    }
    
    /**
     * Busca categoria por ID
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable String categoryId, Authentication auth) {
        try {
            String userId = auth.getName();
            
            return categoryService.getCategoryById(categoryId)
                    .filter(category -> categoryService.canUserUseCategory(userId, categoryId))
                    .map(category -> ResponseEntity.ok(
                            ApiResponse.success(convertToResponse(category), "Categoria encontrada")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Categoria não encontrada ou sem permissão de acesso")));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao buscar categoria: " + e.getMessage()));
        }
    }
    
    /**
     * Cria uma nova categoria personalizada
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request, Authentication auth) {
        try {
            String userId = auth.getName();
            
            Category category = categoryService.createUserCategory(
                    userId,
                    request.getName(),
                    request.getDescription(),
                    request.getColor(),
                    request.getIcon(),
                    request.getType()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(convertToResponse(category), "Categoria criada com sucesso"));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao criar categoria: " + e.getMessage()));
        }
    }
    
    /**
     * Atualiza uma categoria personalizada
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody CategoryUpdateRequest request,
            Authentication auth) {
        try {
            String userId = auth.getName();
            
            Category category = categoryService.updateUserCategory(
                    userId,
                    categoryId,
                    request.getName(),
                    request.getDescription(),
                    request.getColor(),
                    request.getIcon()
            );
            
            return ResponseEntity.ok(ApiResponse.success(convertToResponse(category), 
                    "Categoria atualizada com sucesso"));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao atualizar categoria: " + e.getMessage()));
        }
    }
    
    /**
     * Desativa uma categoria personalizada
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deactivateCategory(
            @PathVariable String categoryId, Authentication auth) {
        try {
            String userId = auth.getName();
            categoryService.deactivateUserCategory(userId, categoryId);
            
            return ResponseEntity.ok(ApiResponse.success(null, "Categoria desativada com sucesso"));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao desativar categoria: " + e.getMessage()));
        }
    }
    
    /**
     * Reativa uma categoria personalizada
     */
    @PatchMapping("/{categoryId}/activate")
    public ResponseEntity<ApiResponse<CategoryResponse>> reactivateCategory(
            @PathVariable String categoryId, Authentication auth) {
        try {
            String userId = auth.getName();
            categoryService.reactivateUserCategory(userId, categoryId);
            
            return categoryService.getCategoryById(categoryId)
                    .map(category -> ResponseEntity.ok(
                            ApiResponse.success(convertToResponse(category), "Categoria reativada com sucesso")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Categoria não encontrada")));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao reativar categoria: " + e.getMessage()));
        }
    }
    
    /**
     * Lista categorias mais utilizadas pelo usuário
     */
    @GetMapping("/most-used")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getMostUsedCategories(Authentication auth) {
        try {
            String userId = auth.getName();
            List<Category> categories = categoryService.getMostUsedCategories(userId);
            
            List<CategoryResponse> response = categories.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(response, "Categorias mais utilizadas carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar categorias mais utilizadas: " + e.getMessage()));
        }
    }
    
    /**
     * Lista apenas categorias personalizadas do usuário
     */
    @GetMapping("/custom")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCustomCategories(Authentication auth) {
        try {
            String userId = auth.getName();
            List<Category> categories = categoryService.getUserCustomCategories(userId);
            
            List<CategoryResponse> response = categories.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(response, "Categorias personalizadas carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar categorias personalizadas: " + e.getMessage()));
        }
    }
    
    /**
     * Obtém estatísticas das categorias do usuário
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<CategoryStatsResponse>> getCategoryStats(Authentication auth) {
        try {
            String userId = auth.getName();
            long customCount = categoryService.countUserCategories(userId);
            boolean canCreateMore = categoryService.canCreateMoreCategories(userId);
            
            CategoryStatsResponse stats = new CategoryStatsResponse();
            stats.setCustomCategoriesCount(customCount);
            stats.setCanCreateMore(canCreateMore);
            stats.setMaxCategories(50L); // Limite configurável
            
            return ResponseEntity.ok(ApiResponse.success(stats, "Estatísticas carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar estatísticas: " + e.getMessage()));
        }
    }
    
    // Método utilitário para conversão
    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setColor(category.getColor());
        response.setIcon(category.getIcon());
        response.setType(category.getType().toString());
        response.setIsSystemDefault(category.getIsSystemDefault());
        response.setActive(category.getActive());
        response.setUserId(category.getUserId());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
} 