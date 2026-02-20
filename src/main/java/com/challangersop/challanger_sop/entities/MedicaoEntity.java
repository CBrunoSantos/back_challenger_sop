package com.challangersop.challanger_sop.entities;

import jakarta.persistence.*;

import com.challangersop.challanger_sop.enums.MedicaoStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "medicao")
public class MedicaoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_measure", nullable = false, unique = true, length = 40)
    private String numero;

    @Column(name = "date_measure", nullable = false)
    private LocalDate dataMedicao;

    @Column(name = "value_measure", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "medicao_status")
    private MedicaoStatus status;

    @Column(name = "observacao")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orc_id", nullable = false)
    private OrcamentoEntity orcamento;

    public MedicaoEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public MedicaoStatus getStatus() {
        return this.status;
    }

    public void setStatus(MedicaoStatus status) {
        this.status = status;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public OrcamentoEntity getOrcamento() {
        return this.orcamento;
    }

    public void setOrcamento(OrcamentoEntity orcamento) {
        this.orcamento = orcamento;
    }
}
