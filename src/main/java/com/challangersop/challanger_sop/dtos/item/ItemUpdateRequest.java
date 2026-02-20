package com.challangersop.challanger_sop.dtos.item;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ItemUpdateRequest {

    @NotBlank
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal quantidade;

    @NotNull
    @Positive
    private BigDecimal valorUnitario;

}
