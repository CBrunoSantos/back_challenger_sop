package com.challangersop.challanger_sop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challangersop.challanger_sop.entities.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>{
    List<ItemEntity> findAllByOrcamentoId(Long orcamentoId);
}
