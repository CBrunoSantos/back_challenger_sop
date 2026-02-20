package com.challangersop.challanger_sop.dtos.orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.challangersop.challanger_sop.entities.OrcamentoEntity;
import com.challangersop.challanger_sop.enums.OrcamentoStatus;
import com.challangersop.challanger_sop.enums.OrcamentoTipo;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrcamentoResponse {

    private Long id;

    private String numeroProtocolo;

    private OrcamentoTipo tipo;

    private BigDecimal valorTotal;

    private LocalDate dataCriacao;

    private OrcamentoStatus status;

    public static OrcamentoResponse from(OrcamentoEntity entity){
        return OrcamentoResponse.builder()
            .id(entity.getId())
            .numeroProtocolo(entity.getNumeroProtocolo())
            .tipo(entity.getTipo())
            .valorTotal(entity.getValorTotal())
            .dataCriacao(entity.getDataCriacao())
            .status(entity.getStatus())
            .build();
    }
}
