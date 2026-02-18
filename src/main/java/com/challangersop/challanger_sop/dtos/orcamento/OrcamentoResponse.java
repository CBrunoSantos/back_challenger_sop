package com.challangersop.challanger_sop.dtos.orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.challangersop.challanger_sop.entities.OrcamentoEntity;
import com.challangersop.challanger_sop.enums.OrcamentoStatus;
import com.challangersop.challanger_sop.enums.OrcamentoTipo;

public class OrcamentoResponse {

    private Long id;

    private String numeroProtocolo;

    private OrcamentoTipo tipo;

    private BigDecimal valorTotal;

    private LocalDate dataCriacao;

    private OrcamentoStatus status;

    public static OrcamentoResponse from(OrcamentoEntity entity){
        OrcamentoResponse res = new OrcamentoResponse();
        res.id = entity.getId();
        res.numeroProtocolo = entity.getNumeroProtocolo();
        res.tipo = entity.getTipo();
        res.valorTotal = entity.getValorTotal();
        res.dataCriacao = entity.getDataCriacao();
        res.status = entity.getStatus();

        return res;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public OrcamentoTipo getTipo() {
        return tipo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public OrcamentoStatus getStatus() {
        return status;
    }
}
