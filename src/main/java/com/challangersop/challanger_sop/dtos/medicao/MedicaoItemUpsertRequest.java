package com.challangersop.challanger_sop.dtos.medicao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class MedicaoItemUpsertRequest {

    @NotNull
    private Long itemId;

    @NotNull
    @Positive
    private BigDecimal quantidadeMedida;

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getQuantidadeMedida() {
        return this.quantidadeMedida;
    }

    public void setQuantidadeMedida(BigDecimal quantidadeMedida) {
        this.quantidadeMedida = quantidadeMedida;
    }
}
