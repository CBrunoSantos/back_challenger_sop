package com.challangersop.challanger_sop.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item")
public class ItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, length = 255)
    private String descricao;

    @Column(name = "quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "unit_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "quantity_amount", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantidadeAcumulada;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orc_id", nullable = false)
    private OrcamentoEntity orcamento;

    protected ItemEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return this.valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getQuantidadeAcumulada() {
        return this.quantidadeAcumulada;
    }

    public void setQuantidadeAcumulada(BigDecimal quantidadeAcumulada) {
        this.quantidadeAcumulada = quantidadeAcumulada;
    }

    public OrcamentoEntity getOrcamento() {
        return this.orcamento;
    }

    public void setOrcamento(OrcamentoEntity orcamento) {
        this.orcamento = orcamento;
    }
}
