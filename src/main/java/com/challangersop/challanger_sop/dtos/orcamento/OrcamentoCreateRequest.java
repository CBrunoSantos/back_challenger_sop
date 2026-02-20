package com.challangersop.challanger_sop.dtos.orcamento;

import java.math.BigDecimal;

import com.challangersop.challanger_sop.enums.OrcamentoTipo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrcamentoCreateRequest {

    @NotBlank
    private String numeroProtocolo;

    @NotNull
    private OrcamentoTipo tipo;

    @NotNull
    @PositiveOrZero
    private BigDecimal valorTotal;

}
