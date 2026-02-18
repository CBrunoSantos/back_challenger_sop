package com.challangersop.challanger_sop.dtos.medicao;

import com.challangersop.challanger_sop.entities.MedicaoEntity;
import com.challangersop.challanger_sop.enums.MedicaoStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicaoResponse {

    private Long id;

    private String numero;

    private LocalDate dataMedicao;

    private BigDecimal valorTotal;

    private MedicaoStatus status;

    private String observacao;

    private Long orcamentoId;

    public static MedicaoResponse from(MedicaoEntity entity) {
        MedicaoResponse res = new MedicaoResponse();
        res.id = entity.getId();
        res.numero = entity.getNumero();
        res.dataMedicao = entity.getDataMedicao();
        res.valorTotal = entity.getValorTotal();
        res.status = entity.getStatus();
        res.observacao = entity.getObservacao();
        res.orcamentoId = entity.getOrcamento().getId();
        return res;
    }

    public Long getId() {
        return this.id;
    }

    public String getNumero() {
        return this.numero;
    }

    public LocalDate getDataMedicao() {
        return this.dataMedicao;
    }

    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }

    public MedicaoStatus getStatus() {
        return this.status;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Long getOrcamentoId() {
        return this.orcamentoId;
    }
}
