package com.finnantech.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finnantech.domain.entities.Category;
import com.finnantech.domain.entities.Transaction;
import com.finnantech.infrastructure.persistence.repositories.CategoryRepository;
import com.finnantech.infrastructure.persistence.repositories.TransactionRepository;
import com.finnantech.infrastructure.web.dtos.CategoryStatsResponse;
import com.finnantech.infrastructure.web.dtos.DashboardSummaryResponse;
import com.finnantech.infrastructure.web.dtos.MonthlyExpensesResponse;
import com.finnantech.infrastructure.web.dtos.RecentTransactionResponse;

/**
 * Service para dashboard com dados reais do banco H2
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Obtém resumo financeiro geral baseado em dados reais
     */
    public DashboardSummaryResponse getDashboardSummary(String userId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        
        // Mês anterior
        LocalDate firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);
        LocalDate lastDayOfLastMonth = firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth());

        // Totais do mês atual
        BigDecimal currentMonthIncomes = transactionRepository.getTotalIncomeByUserAndPeriod(userId, firstDayOfMonth, lastDayOfMonth);
        BigDecimal currentMonthExpenses = transactionRepository.getTotalExpensesByUserAndPeriod(userId, firstDayOfMonth, lastDayOfMonth);
        
        // Totais do mês anterior
        BigDecimal lastMonthIncomes = transactionRepository.getTotalIncomeByUserAndPeriod(userId, firstDayOfLastMonth, lastDayOfLastMonth);
        BigDecimal lastMonthExpenses = transactionRepository.getTotalExpensesByUserAndPeriod(userId, firstDayOfLastMonth, lastDayOfLastMonth);

        // Calcular variações percentuais
        BigDecimal incomesVariation = calculatePercentageVariation(lastMonthIncomes, currentMonthIncomes);
        BigDecimal expensesVariation = calculatePercentageVariation(lastMonthExpenses, currentMonthExpenses);
        
        // Saldos
        BigDecimal currentBalance = currentMonthIncomes.subtract(currentMonthExpenses);
        BigDecimal lastBalance = lastMonthIncomes.subtract(lastMonthExpenses);
        BigDecimal balanceVariation = calculatePercentageVariation(lastBalance, currentBalance);

        // Estatísticas adicionais
        List<Transaction> allTransactions = transactionRepository.findByUserAndDateRange(userId, firstDayOfMonth, lastDayOfMonth);
        int transactionCount = allTransactions.size();
        
        BigDecimal averageAmount = BigDecimal.ZERO;
        if (transactionCount > 0) {
            BigDecimal totalAmount = allTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            averageAmount = totalAmount.divide(BigDecimal.valueOf(transactionCount), 2, RoundingMode.HALF_UP);
        }

        DashboardSummaryResponse summary = new DashboardSummaryResponse();
        summary.setTotalReceitas(currentMonthIncomes);
        summary.setTotalDespesas(currentMonthExpenses);
        summary.setSaldoAtual(currentBalance);
        summary.setSaldoAnterior(lastBalance);
        summary.setVariacaoReceitas(incomesVariation);
        summary.setVariacaoDespesas(expensesVariation);
        summary.setVariacaoSaldo(balanceVariation);
        summary.setQuantidadeTransacoes(transactionCount);
        summary.setPeriodoReferencia(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        summary.setMetaMensal(BigDecimal.valueOf(5000)); // Meta padrão, pode ser configurável
        summary.setGastoMedio(averageAmount);
        summary.setEconomiaEsperada(currentBalance.max(BigDecimal.ZERO));
        summary.setUltimaAtualizacao(LocalDateTime.now());
        
        return summary;
    }

    /**
     * Obtém resumo mensal específico baseado em dados reais
     */
    public DashboardSummaryResponse getMonthlySummary(String userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        BigDecimal totalIncomes = transactionRepository.getTotalIncomeByUserAndPeriod(userId, firstDay, lastDay);
        BigDecimal totalExpenses = transactionRepository.getTotalExpensesByUserAndPeriod(userId, firstDay, lastDay);
        BigDecimal balance = totalIncomes.subtract(totalExpenses);

        List<Transaction> monthTransactions = transactionRepository.findByUserAndDateRange(userId, firstDay, lastDay);
        int transactionCount = monthTransactions.size();

        DashboardSummaryResponse summary = new DashboardSummaryResponse();
        summary.setTotalReceitas(totalIncomes);
        summary.setTotalDespesas(totalExpenses);
        summary.setSaldoAtual(balance);
        summary.setQuantidadeTransacoes(transactionCount);
        summary.setPeriodoReferencia(yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        summary.setUltimaAtualizacao(LocalDateTime.now());
        
        return summary;
    }

    /**
     * Obtém estatísticas por categoria baseado em dados reais
     */
    public List<CategoryStatsResponse> getCategoryStats(String userId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        // Buscar gastos por categoria
        List<Object[]> expensesByCategory = transactionRepository.getExpensesByCategoryAndPeriod(userId, firstDayOfMonth, lastDayOfMonth);
        
        // Calcular total de despesas para percentuais
        BigDecimal totalExpenses = expensesByCategory.stream()
            .map(row -> (BigDecimal) row[1])
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Buscar informações das categorias
        Map<String, Category> categoriesMap = categoryRepository.findAllAvailableForUser(userId)
            .stream()
            .collect(Collectors.toMap(Category::getId, category -> category));

        List<CategoryStatsResponse> categoryStats = new ArrayList<>();
        
        for (Object[] row : expensesByCategory) {
            String categoryId = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            
            Category category = categoriesMap.get(categoryId);
            if (category == null) continue;

            // Contar transações da categoria
            List<Transaction> categoryTransactions = transactionRepository.findByUserAndCategory(userId, categoryId);
            int transactionCount = categoryTransactions.size();

            // Calcular percentual
            double percentage = totalExpenses.compareTo(BigDecimal.ZERO) > 0 
                ? amount.divide(totalExpenses, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;

            CategoryStatsResponse stats = new CategoryStatsResponse(
                categoryId,
                category.getName(),
                amount,
                transactionCount,
                percentage
            );
            categoryStats.add(stats);
        }

        // Ordenar por valor total (maior primeiro)
        categoryStats.sort((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()));
        
        return categoryStats;
    }

    /**
     * Obtém despesas mensais para gráfico de barras
     */
    public List<MonthlyExpensesResponse> getMonthlyExpenses(String userId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusMonths(5).withDayOfMonth(1);
        LocalDate endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        return getMonthlyExpensesByPeriod(userId, startDate, endDate);
    }

    /**
     * Obtém despesas mensais por período específico
     */
    public List<MonthlyExpensesResponse> getMonthlyExpensesByPeriod(String userId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> monthlyData = transactionRepository.getMonthlyExpenses(userId, startDate, endDate);
        
        // Criar mapa para todos os meses no período
        Map<String, MonthlyExpensesResponse> monthlyExpensesMap = new HashMap<>();
        
        // Inicializar com zeros para todos os meses no período
        LocalDate currentMonth = startDate.withDayOfMonth(1);
        while (!currentMonth.isAfter(endDate)) {
            String monthYear = currentMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String monthName = currentMonth.format(DateTimeFormatter.ofPattern("MMMM"));
            monthlyExpensesMap.put(monthYear, new MonthlyExpensesResponse(monthYear, monthName, BigDecimal.ZERO, 0));
            currentMonth = currentMonth.plusMonths(1);
        }

        // Preencher com dados reais
        for (Object[] row : monthlyData) {
            Integer year = (Integer) row[0];
            Integer month = (Integer) row[1];
            BigDecimal amount = (BigDecimal) row[2];
            
            // Formatar mês-ano como string
            String monthYear = String.format("%d-%02d", year, month);
            
            MonthlyExpensesResponse expenses = monthlyExpensesMap.get(monthYear);
            if (expenses != null) {
                expenses.setDespesas(amount);
            }
        }

        // Contar transações de despesa por mês
        for (MonthlyExpensesResponse expenses : monthlyExpensesMap.values()) {
            LocalDate firstDay = YearMonth.parse(expenses.getMesAno()).atDay(1);
            LocalDate lastDay = YearMonth.parse(expenses.getMesAno()).atEndOfMonth();
            
            List<Transaction> monthExpenseTransactions = transactionRepository.findByUserAndDateRange(userId, firstDay, lastDay)
                .stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.toList());
                
            expenses.setQuantidadeTransacoes(monthExpenseTransactions.size());
        }

        return monthlyExpensesMap.values()
            .stream()
            .sorted(Comparator.comparing(MonthlyExpensesResponse::getMesAno))
            .collect(Collectors.toList());
    }

    /**
     * Compara períodos baseado em dados reais
     */
    public Object comparePeriods(String userId, String period1, String period2) {
        // TODO: Implementar comparação de períodos com dados reais
        return "{\"period1\": \"" + period1 + "\", \"period2\": \"" + period2 + "\", \"difference\": 0.0, \"status\": \"no_data\"}";
    }

    /**
     * Obtém transações recentes baseado em dados reais
     */
    public List<RecentTransactionResponse> getRecentTransactions(String userId) {
        List<Transaction> recentTransactions = transactionRepository.findLatestTransactionsByUser(userId, 10);
        
        // Buscar informações das categorias
        Set<String> categoryIds = recentTransactions.stream()
            .map(Transaction::getCategoryId)
            .collect(Collectors.toSet());
        
        Map<String, Category> categoriesMap = categoryRepository.findAllById(categoryIds)
            .stream()
            .collect(Collectors.toMap(Category::getId, category -> category));

        return recentTransactions.stream()
            .map(transaction -> {
                Category category = categoriesMap.get(transaction.getCategoryId());
                String categoryName = category != null ? category.getName() : "Categoria Desconhecida";
                String categoryIcon = category != null ? category.getIcon() : "❓";

                // Converter tipo da transação para string
                String transactionType = transaction.isIncome() ? "RECEITA" : "DESPESA";
                
                return new RecentTransactionResponse(
                    transaction.getId(),
                    transaction.getDescription(),
                    transaction.getAmount(),
                    transactionType,
                    categoryName,
                    categoryIcon,
                    transaction.getPaymentMethodId() != null ? "Cartão" : "Não informado", // TODO: buscar nome real do método de pagamento
                    transaction.getTransactionDate()
                );
            })
            .collect(Collectors.toList());
    }

    /**
     * Obtém orçamentos baseado em dados reais
     */
    public Object getBudgets(String userId) {
        // TODO: Implementar quando entidade Budget existir
        return "[]";
    }

    /**
     * Calcula variação percentual entre dois valores
     */
    private BigDecimal calculatePercentageVariation(BigDecimal oldValue, BigDecimal newValue) {
        if (oldValue.compareTo(BigDecimal.ZERO) == 0) {
            return newValue.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
        }
        
        BigDecimal difference = newValue.subtract(oldValue);
        return difference.divide(oldValue, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
    }
} 