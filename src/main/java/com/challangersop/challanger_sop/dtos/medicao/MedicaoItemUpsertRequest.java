package com.challangersop.challanger_sop.dtos.medicao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicaoItemUpsertRequest {

    @NotNull
    private Long itemId;

    @NotNull
    @Positive
    private BigDecimal quantidadeMedida;

}
