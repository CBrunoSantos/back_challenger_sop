package com.challangersop.challanger_sop.dtos.medicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class MedicaoCreateRequest {

    @NotBlank
    private String numero;

    @NotNull
    private LocalDate dataMedicao;

    @NotNull
    private Long orcamentoId;

    private String observacao;

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDataMedicao() {
        return this.dataMedicao;
    }

    public void setDataMedicao(LocalDate dataMedicao) {
        this.dataMedicao = dataMedicao;
    }

    public Long getOrcamentoId() {
        return this.orcamentoId;
    }

    public void setOrcamentoId(Long orcamentoId) {
        this.orcamentoId = orcamentoId;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
