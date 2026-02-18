package com.challangersop.challanger_sop.dtos.medicao;

import com.challangersop.challanger_sop.entities.ItemMedicaoEntity;

import java.math.BigDecimal;

public class MedicaoItemResponse {

    private Long id;

    private Long medicaoId;

    private Long itemId;

    private BigDecimal quantidadeMedida;

    private BigDecimal valorUnitarioAplicado;

    private BigDecimal valorTotalMedido;

    public static MedicaoItemResponse from(ItemMedicaoEntity entity) {
        MedicaoItemResponse res = new MedicaoItemResponse();
        res.id = entity.getId();
        res.medicaoId = entity.getMedicao().getId();
        res.itemId = entity.getItem().getId();
        res.quantidadeMedida = entity.getQuantidadeMedida();
        res.valorUnitarioAplicado = entity.getValorUnitarioAplicado();
        res.valorTotalMedido = entity.getValorTotalMedido();
        return res;
    }

    public Long getId() {
        return this.id;
    }

    public Long getMedicaoId() {
        return this.medicaoId;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public BigDecimal getQuantidadeMedida() {
        return this.quantidadeMedida;
    }

    public BigDecimal getValorUnitarioAplicado() {
        return this.valorUnitarioAplicado;
    }

    public BigDecimal getValorTotalMedido() {
        return this.valorTotalMedido;
    }
}
