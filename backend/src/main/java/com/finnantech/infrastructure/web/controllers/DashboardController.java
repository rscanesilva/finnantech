package com.finnantech.infrastructure.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finnantech.application.services.DashboardService;
import com.finnantech.infrastructure.services.JwtServiceAdapter;
import com.finnantech.infrastructure.web.dtos.ApiResponse;
import com.finnantech.infrastructure.web.dtos.CategoryStatsResponse;
import com.finnantech.infrastructure.web.dtos.DashboardSummaryResponse;
import com.finnantech.infrastructure.web.dtos.MonthlyExpensesResponse;
import com.finnantech.infrastructure.web.dtos.RecentTransactionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller REST para dashboard e estatísticas financeiras
 * Endpoints para dados de resumo, gráficos e métricas
 */
@RestController
@RequestMapping("/v1/dashboard")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
@Tag(name = "Dashboard", description = "Endpoints para dashboard e estatísticas financeiras")
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardController {

    private final DashboardService dashboardService;
    private final JwtServiceAdapter jwtService;

    @Autowired
    public DashboardController(DashboardService dashboardService, JwtServiceAdapter jwtService) {
        this.dashboardService = dashboardService;
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
     * Obter resumo financeiro geral do usuário
     */
    @GetMapping("/summary")
    @Operation(summary = "Resumo financeiro", description = "Obtém resumo geral das finanças do usuário")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            DashboardSummaryResponse summary = dashboardService.getDashboardSummary(userId);
            
            return ResponseEntity.ok(ApiResponse.success(summary, "Resumo carregado com sucesso"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar resumo: " + e.getMessage()));
        }
    }

    /**
     * Obter resumo financeiro de um período específico
     */
    @GetMapping("/summary/{year}/{month}")
    @Operation(summary = "Resumo mensal", description = "Obtém resumo financeiro de um mês específico")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getMonthlySummary(
            @PathVariable int year, 
            @PathVariable int month, 
            HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            DashboardSummaryResponse summary = dashboardService.getMonthlySummary(userId, year, month);
            
            return ResponseEntity.ok(ApiResponse.success(summary, "Resumo mensal carregado"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erro ao carregar resumo mensal: " + e.getMessage()));
        }
    }

    /**
     * Obter estatísticas por categoria do mês atual
     */
    @GetMapping("/categories/stats")
    @Operation(summary = "Estatísticas por categoria", description = "Estatísticas de gastos por categoria")
    public ResponseEntity<ApiResponse<List<CategoryStatsResponse>>> getCategoryStats(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            List<CategoryStatsResponse> stats = dashboardService.getCategoryStats(userId);
            
            return ResponseEntity.ok(ApiResponse.success(stats, "Estatísticas carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar estatísticas: " + e.getMessage()));
        }
    }

    /**
     * Obter despesas mensais dos últimos 6 meses (para gráfico de barras)
     */
    @GetMapping("/expenses/monthly")
    @Operation(summary = "Despesas mensais", description = "Despesas mensais dos últimos 6 meses para gráfico de barras")
    public ResponseEntity<ApiResponse<List<MonthlyExpensesResponse>>> getMonthlyExpenses(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            List<MonthlyExpensesResponse> expenses = dashboardService.getMonthlyExpenses(userId);
            
            return ResponseEntity.ok(ApiResponse.success(expenses, "Despesas mensais carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar despesas mensais: " + e.getMessage()));
        }
    }

    /**
     * Obter despesas mensais por período específico
     */
    @GetMapping("/expenses/monthly/period")
    @Operation(summary = "Despesas mensais por período", description = "Despesas mensais filtradas por período específico")
    public ResponseEntity<ApiResponse<List<MonthlyExpensesResponse>>> getMonthlyExpensesByPeriod(
            @RequestParam String startDate,  // formato: YYYY-MM-DD
            @RequestParam String endDate,    // formato: YYYY-MM-DD
            HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            
            // Converter strings para LocalDate
            java.time.LocalDate start = java.time.LocalDate.parse(startDate);
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);
            
            List<MonthlyExpensesResponse> expenses = dashboardService.getMonthlyExpensesByPeriod(userId, start, end);
            
            return ResponseEntity.ok(ApiResponse.success(expenses, "Despesas mensais do período carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erro ao carregar despesas do período: " + e.getMessage()));
        }
    }

    /**
     * Obter comparativo entre dois períodos
     */
    @GetMapping("/compare")
    @Operation(summary = "Comparar períodos", description = "Compara receitas e despesas entre dois períodos")
    public ResponseEntity<ApiResponse<Object>> comparePeriods(
            @RequestParam String period1,  // formato: YYYY-MM
            @RequestParam String period2,  // formato: YYYY-MM
            HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            Object comparison = dashboardService.comparePeriods(userId, period1, period2);
            
            return ResponseEntity.ok(ApiResponse.success(comparison, "Comparação realizada"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erro na comparação: " + e.getMessage()));
        }
    }

    /**
     * Obter transações recentes (últimas 10)
     */
    @GetMapping("/recent-transactions")
    @Operation(summary = "Transações recentes", description = "Últimas 10 transações do usuário")
    public ResponseEntity<ApiResponse<List<RecentTransactionResponse>>> getRecentTransactions(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            List<RecentTransactionResponse> transactions = dashboardService.getRecentTransactions(userId);
            
            return ResponseEntity.ok(ApiResponse.success(transactions, "Transações recentes carregadas"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar transações: " + e.getMessage()));
        }
    }

    /**
     * Obter metas e orçamentos
     */
    @GetMapping("/budgets")
    @Operation(summary = "Metas e orçamentos", description = "Metas de gastos e orçamentos por categoria")
    public ResponseEntity<ApiResponse<Object>> getBudgets(HttpServletRequest request) {
        try {
            String userId = extractUserIdFromRequest(request);
            Object budgets = dashboardService.getBudgets(userId);
            
            return ResponseEntity.ok(ApiResponse.success(budgets, "Orçamentos carregados"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao carregar orçamentos: " + e.getMessage()));
        }
    }
} 