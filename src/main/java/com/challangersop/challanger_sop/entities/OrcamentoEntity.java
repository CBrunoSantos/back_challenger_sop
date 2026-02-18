package com.challangersop.challanger_sop.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.challangersop.challanger_sop.enums.OrcamentoStatus;
import com.challangersop.challanger_sop.enums.OrcamentoTipo;

import jakarta.persistence.*;

@Entity
@Table(name = "orcamento")
public class OrcamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "num_protcl", nullable = false, unique = true, length = 40)
    private String numeroProtocolo;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type_orc", nullable = false, columnDefinition = "orcamento_tipo")
    private OrcamentoTipo tipo;

    @Column(name = "total_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "create_at", nullable = false)
    private LocalDate dataCriacao;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "orcamento_status")
    private OrcamentoStatus status;

    protected OrcamentoEntity(){
    }

    public long getId() {
        return id;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public OrcamentoTipo getTipo() {
        return tipo;
    }

    public void setTipo(OrcamentoTipo tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public OrcamentoStatus getStatus() {
        return status;
    }

    public void setStatus(OrcamentoStatus status) {
        this.status = status;
    }
}
