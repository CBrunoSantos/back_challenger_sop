package com.challangersop.challanger_sop.dtos.item;

import java.math.BigDecimal;

import com.challangersop.challanger_sop.entities.ItemEntity;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ItemResponse {

    private Long id;

    private String descricao;

    private BigDecimal quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    private BigDecimal quantidadeAcumulada;

    private Long orcamentoId;

    public static ItemResponse from(ItemEntity entity) {
        return ItemResponse.builder()
            .id(entity.getId())
            .descricao(entity.getDescricao())
            .quantidade(entity.getQuantidade())
            .valorUnitario(entity.getValorUnitario())
            .valorTotal(entity.getValorTotal())
            .quantidadeAcumulada(entity.getQuantidadeAcumulada())
            .orcamentoId(entity.getOrcamento().getId())
            .build();
    }
}
