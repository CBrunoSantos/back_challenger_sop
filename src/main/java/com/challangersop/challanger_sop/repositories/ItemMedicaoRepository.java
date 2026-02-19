package com.challangersop.challanger_sop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challangersop.challanger_sop.entities.ItemMedicaoEntity;

public interface ItemMedicaoRepository extends JpaRepository<ItemMedicaoEntity, Long>{
    Optional<ItemMedicaoEntity> findByMedicaoIdAndItemId(Long medicaoId, Long itemId);
    List<ItemMedicaoEntity> findAllByMedicaoId(Long medicaoId);
    List<ItemMedicaoEntity> findByMedicaoIdOrderByIdAsc(Long medicaoId);
}
