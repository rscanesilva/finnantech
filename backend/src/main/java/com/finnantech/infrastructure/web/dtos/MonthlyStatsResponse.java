package com.finnantech.infrastructure.web.dtos;

import java.math.BigDecimal;

/**
 * DTO para estatísticas mensais
 */
public class MonthlyStatsResponse {
    
    private String mesAno; // formato: "2024-03"
    private BigDecimal receitas;
    private BigDecimal despesas;
    private BigDecimal saldo;
    private Integer quantidadeTransacoes;

    // Construtores
    public MonthlyStatsResponse() {}

    public MonthlyStatsResponse(String mesAno, BigDecimal receitas, BigDecimal despesas, 
                              BigDecimal saldo, Integer quantidadeTransacoes) {
        this.mesAno = mesAno;
        this.receitas = receitas;
        this.despesas = despesas;
        this.saldo = saldo;
        this.quantidadeTransacoes = quantidadeTransacoes;
    }

    // Getters e Setters
    public String getMesAno() {
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

    public BigDecimal getReceitas() {
        return receitas;
    }

    public void setReceitas(BigDecimal receitas) {
        this.receitas = receitas;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public void setDespesas(BigDecimal despesas) {
        this.despesas = despesas;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Integer getQuantidadeTransacoes() {
        return quantidadeTransacoes;
    }

    public void setQuantidadeTransacoes(Integer quantidadeTransacoes) {
        this.quantidadeTransacoes = quantidadeTransacoes;
    }
} 