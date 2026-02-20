package com.challangersop.challanger_sop.dtos.medicao;

import com.challangersop.challanger_sop.entities.MedicaoEntity;
import com.challangersop.challanger_sop.enums.MedicaoStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicaoResponse {

    private Long id;

    private String numero;

    private LocalDate dataMedicao;

    private BigDecimal valorTotal;

    private MedicaoStatus status;

    private String observacao;

    private Long orcamentoId;

    public static MedicaoResponse from(MedicaoEntity entity) {
        return MedicaoResponse.builder()
            .id(entity.getId())
            .numero(entity.getNumero())
            .dataMedicao(entity.getDataMedicao())
            .valorTotal(entity.getValorTotal())
            .status(entity.getStatus())
            .observacao(entity.getObservacao())
            .orcamentoId(entity.getOrcamento().getId())
            .build();
    }
}
