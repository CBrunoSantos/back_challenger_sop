package com.challangersop.challanger_sop.dtos.orcamento;

import java.math.BigDecimal;

import com.challangersop.challanger_sop.enums.OrcamentoTipo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class OrcamentoUpdateRequest {

    @NotNull
    private OrcamentoTipo tipo;

    @NotNull
    @PositiveOrZero
    private BigDecimal valorTotal;

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
