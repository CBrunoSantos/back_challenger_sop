package com.challangersop.challanger_sop.dtos.item;

import java.math.BigDecimal;

import com.challangersop.challanger_sop.entities.ItemEntity;

public class ItemResponse {

    private Long id;

    private String descricao;

    private BigDecimal quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    private BigDecimal quantidadeAcumulada;

    private Long orcamentoId;

    public static ItemResponse from(ItemEntity entity) {
        ItemResponse res = new ItemResponse();
        res.id = entity.getId();
        res.descricao = entity.getDescricao();
        res.quantidade = entity.getQuantidade();
        res.valorUnitario = entity.getValorUnitario();
        res.valorTotal = entity.getValorTotal();
        res.quantidadeAcumulada = entity.getQuantidadeAcumulada();
        res.orcamentoId = entity.getOrcamento().getId();
        return res;
    }

    public Long getId() {
        return this.id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public BigDecimal getQuantidade() {
        return this.quantidade;
    }

    public BigDecimal getValorUnitario() {
        return this.valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }

    public BigDecimal getQuantidadeAcumulada() {
        return this.quantidadeAcumulada;
    }

    public Long getOrcamentoId() {
        return this.orcamentoId;
    }
}
