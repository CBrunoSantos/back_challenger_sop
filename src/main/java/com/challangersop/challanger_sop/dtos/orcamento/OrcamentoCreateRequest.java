package com.challangersop.challanger_sop.dtos.orcamento;

import java.math.BigDecimal;

import com.challangersop.challanger_sop.enums.OrcamentoTipo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class OrcamentoCreateRequest {

    @NotBlank
    private String numeroProtocolo;

    @NotNull
    private OrcamentoTipo tipo;

    @NotNull
    @PositiveOrZero
    private BigDecimal valorTotal;

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public OrcamentoTipo getTipo() {
        return tipo;
    }

    public void setTipo(OrcamentoTipo tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

}
