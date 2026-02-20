package com.challangersop.challanger_sop.dtos.medicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicaoCreateRequest {

    @NotBlank
    private String numero;

    @NotNull
    private LocalDate dataMedicao;

    @NotNull
    private Long orcamentoId;

    private String observacao;
}
