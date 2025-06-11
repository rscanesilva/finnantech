package com.finnantech.infrastructure.web.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finnantech.application.services.CategoryService;
import com.finnantech.domain.entities.Category;
import com.finnantech.infrastructure.services.JwtServiceAdapter;
import com.finnantech.infrastructure.web.dtos.ApiResponse;
import com.finnantech.infrastructure.web.dtos.CategoryCreateRequest;
import com.finnantech.infrastructure.web.dtos.CategoryResponse;
import com.finnantech.infrastructure.web.dtos.CategoryUpdateRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller REST para gerenciamento de categorias
 * Endpoints para CRUD de categorias e consultas especializadas
 */
@RestController
@RequestMapping("/v1/categories")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
@Tag(name = "Categories", description = "Endpoints para gerenciamento de categorias")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {
    
    private final CategoryService categoryService;
    private final JwtServiceAdapter jwtService;
    
    @Autowired
    public CategoryController(CategoryService categoryService, JwtServiceAdapter jwtService) {
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }
    
    /**
     * Extrai o userId do token JWT
     */
    private String extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUserId(token);
        }
        throw new RuntimeException("Token JWT não encontrado");
    }
    
    /**
     * Lista todas as categorias disponíveis para o usuário
     * (categorias próprias + categorias padrão do sistema)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
            @PathVariable String type, HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getExpenseCategories(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getIncomeCategories(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getInvestmentCategories(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
            @PathVariable String categoryId, HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            
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
            @Valid @RequestBody CategoryCreateRequest request, HttpServletRequest httpRequest) {
        try {
            String userId = extractUserIdFromRequest(httpRequest);
            
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
            HttpServletRequest httpRequest) {
        try {
            String userId = extractUserIdFromRequest(httpRequest);
            
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
            @PathVariable String categoryId, HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
            @PathVariable String categoryId, HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            categoryService.reactivateUserCategory(userId, categoryId);
            
            return categoryService.getCategoryById(categoryId)
                    .map(category -> ResponseEntity.ok(
                            ApiResponse.success(convertToResponse(category), "Categoria reativada com sucesso")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Categoria não encontrada")));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao reativar categoria: " + e.getMessage()));
        }
    }
    
    /**
     * Lista categorias mais utilizadas pelo usuário
     */
    @GetMapping("/most-used")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getMostUsedCategories(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCustomCategories(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
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
    public ResponseEntity<ApiResponse<Object>> getCategoryStats(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            long customCount = categoryService.countUserCategories(userId);
            boolean canCreateMore = categoryService.canCreateMoreCategories(userId);
            
            // TODO: Criar DTO específico para estatísticas de categorias do usuário
            // Por enquanto retornando objeto genérico
            Object stats = "{\"customCategoriesCount\": " + customCount + 
                          ", \"canCreateMore\": " + canCreateMore + 
                          ", \"maxCategories\": 50}";
            
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