package com.challangersop.challanger_sop.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.challangersop.challanger_sop.entities.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>{
    List<ItemEntity> findAllByOrcamentoId(Long orcamentoId);

    @Query("select coalesce(sum(i.quantidade * i.valorUnitario), 0) from ItemEntity i where i.orcamento.id = :orcamentoId")
    BigDecimal sumValorItensByOrcamentoId(Long orcamentoId);
}
