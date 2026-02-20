package com.challangersop.challanger_sop.dtos.medicao;

import com.challangersop.challanger_sop.entities.ItemMedicaoEntity;

import java.math.BigDecimal;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicaoItemResponse {

    private Long id;

    private Long medicaoId;

    private Long itemId;

    private BigDecimal quantidadeMedida;

    private BigDecimal valorUnitarioAplicado;

    private BigDecimal valorTotalMedido;

    public static MedicaoItemResponse from(ItemMedicaoEntity entity) {
        return MedicaoItemResponse.builder()
            .id(entity.getId())
            .medicaoId(entity.getMedicao().getId())
            .itemId(entity.getItem().getId())
            .quantidadeMedida(entity.getQuantidadeMedida())
            .valorUnitarioAplicado(entity.getValorUnitarioAplicado())
            .valorTotalMedido(entity.getValorTotalMedido())
            .build();
    }

}
