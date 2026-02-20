package com.challangersop.challanger_sop.entities;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(
    name = "item_medicao",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_item_por_medicao", columnNames = {"measure_id", "item_id"})
    }
)
public class ItemMedicaoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qtd_measure", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantidadeMedida;

    @Column(name = "unit_value_applied", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitarioAplicado;

    @Column(name = "total_value_msr", precision = 15, scale = 2, insertable = false, updatable = false)
    private BigDecimal valorTotalMedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "measure_id", nullable = false)
    private MedicaoEntity medicao;

    public ItemMedicaoEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantidadeMedida() {
        return this.quantidadeMedida;
    }

    public void setQuantidadeMedida(BigDecimal quantidadeMedida) {
        this.quantidadeMedida = quantidadeMedida;
    }

    public BigDecimal getValorUnitarioAplicado() {
        return this.valorUnitarioAplicado;
    }

    public void setValorUnitarioAplicado(BigDecimal valorUnitarioAplicado) {
        this.valorUnitarioAplicado = valorUnitarioAplicado;
    }

    public BigDecimal getValorTotalMedido() {
        return this.valorTotalMedido;
    }

    public ItemEntity getItem() {
        return this.item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public MedicaoEntity getMedicao() {
        return this.medicao;
    }

    public void setMedicao(MedicaoEntity medicao) {
        this.medicao = medicao;
    }
}
