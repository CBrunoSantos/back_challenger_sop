package com.challangersop.challanger_sop.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.challangersop.challanger_sop.entities.ItemEntity;
import com.challangersop.challanger_sop.entities.MedicaoEntity;

import jakarta.persistence.LockModeType;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>{
    List<ItemEntity> findAllByOrcamentoId(Long orcamentoId);

    @Query("select coalesce(sum(i.quantidade * i.valorUnitario), 0) from ItemEntity i where i.orcamento.id = :orcamentoId")
    BigDecimal sumValorItensByOrcamentoId(Long orcamentoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from ItemEntity i where i.id = :id")
    Optional<ItemEntity> findByIdForUpdate(Long id);
}
