package com.finnantech.infrastructure.web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta do resumo do dashboard
 */
public class DashboardSummaryResponse {
    
    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal saldoAtual;
    private BigDecimal saldoAnterior;
    private BigDecimal variacaoReceitas;
    private BigDecimal variacaoDespesas;
    private BigDecimal variacaoSaldo;
    private Integer quantidadeTransacoes;
    private String periodoReferencia;
    private BigDecimal metaMensal;
    private BigDecimal gastoMedio;
    private BigDecimal economiaEsperada;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ultimaAtualizacao;

    // Construtores
    public DashboardSummaryResponse() {}

    public DashboardSummaryResponse(BigDecimal totalReceitas, BigDecimal totalDespesas, 
                                  BigDecimal saldoAtual, Integer quantidadeTransacoes) {
        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.saldoAtual = saldoAtual;
        this.quantidadeTransacoes = quantidadeTransacoes;
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    // Getters e Setters
    public BigDecimal getTotalReceitas() {
        return totalReceitas;
    }

    public void setTotalReceitas(BigDecimal totalReceitas) {
        this.totalReceitas = totalReceitas;
    }

    public BigDecimal getTotalDespesas() {
        return totalDespesas;
    }

    public void setTotalDespesas(BigDecimal totalDespesas) {
        this.totalDespesas = totalDespesas;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getVariacaoReceitas() {
        return variacaoReceitas;
    }

    public void setVariacaoReceitas(BigDecimal variacaoReceitas) {
        this.variacaoReceitas = variacaoReceitas;
    }

    public BigDecimal getVariacaoDespesas() {
        return variacaoDespesas;
    }

    public void setVariacaoDespesas(BigDecimal variacaoDespesas) {
        this.variacaoDespesas = variacaoDespesas;
    }

    public BigDecimal getVariacaoSaldo() {
        return variacaoSaldo;
    }

    public void setVariacaoSaldo(BigDecimal variacaoSaldo) {
        this.variacaoSaldo = variacaoSaldo;
    }

    public Integer getQuantidadeTransacoes() {
        return quantidadeTransacoes;
    }

    public void setQuantidadeTransacoes(Integer quantidadeTransacoes) {
        this.quantidadeTransacoes = quantidadeTransacoes;
    }

    public String getPeriodoReferencia() {
        return periodoReferencia;
    }

    public void setPeriodoReferencia(String periodoReferencia) {
        this.periodoReferencia = periodoReferencia;
    }

    public BigDecimal getMetaMensal() {
        return metaMensal;
    }

    public void setMetaMensal(BigDecimal metaMensal) {
        this.metaMensal = metaMensal;
    }

    public BigDecimal getGastoMedio() {
        return gastoMedio;
    }

    public void setGastoMedio(BigDecimal gastoMedio) {
        this.gastoMedio = gastoMedio;
    }

    public BigDecimal getEconomiaEsperada() {
        return economiaEsperada;
    }

    public void setEconomiaEsperada(BigDecimal economiaEsperada) {
        this.economiaEsperada = economiaEsperada;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
} 