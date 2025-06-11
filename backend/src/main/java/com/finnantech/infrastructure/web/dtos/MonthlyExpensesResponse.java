package com.finnantech.infrastructure.web.dtos;

import java.math.BigDecimal;

/**
 * DTO para despesas mensais - usado no gráfico de barras
 */
public class MonthlyExpensesResponse {
    
    private String mesAno;           // Formato: YYYY-MM (ex: 2025-06)
    private String mesNome;          // Nome do mês (ex: Junho)
    private BigDecimal despesas;     // Total de despesas do mês
    private int quantidadeTransacoes; // Número de transações de despesa
    
    // Construtores
    public MonthlyExpensesResponse() {}
    
    public MonthlyExpensesResponse(String mesAno, String mesNome, BigDecimal despesas, int quantidadeTransacoes) {
        this.mesAno = mesAno;
        this.mesNome = mesNome;
        this.despesas = despesas;
        this.quantidadeTransacoes = quantidadeTransacoes;
    }
    
    // Getters e Setters
    public String getMesAno() {
        return mesAno;
    }
    
    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }
    
    public String getMesNome() {
        return mesNome;
    }
    
    public void setMesNome(String mesNome) {
        this.mesNome = mesNome;
    }
    
    public BigDecimal getDespesas() {
        return despesas;
    }
    
    public void setDespesas(BigDecimal despesas) {
        this.despesas = despesas;
    }
    
    public int getQuantidadeTransacoes() {
        return quantidadeTransacoes;
    }
    
    public void setQuantidadeTransacoes(int quantidadeTransacoes) {
        this.quantidadeTransacoes = quantidadeTransacoes;
    }
    
    @Override
    public String toString() {
        return "MonthlyExpensesResponse{" +
                "mesAno='" + mesAno + '\'' +
                ", mesNome='" + mesNome + '\'' +
                ", despesas=" + despesas +
                ", quantidadeTransacoes=" + quantidadeTransacoes +
                '}';
    }
} 