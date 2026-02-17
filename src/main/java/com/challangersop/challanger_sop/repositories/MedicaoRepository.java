package com.challangersop.challanger_sop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challangersop.challanger_sop.entities.MedicaoEntity;
import com.challangersop.challanger_sop.enums.MedicaoStatus;

public interface MedicaoRepository extends JpaRepository<MedicaoEntity, Long>{
    Optional<MedicaoEntity> findByNumero(String numero);
    boolean existsByOrcamentoAndStatus(Long orcamentoId, MedicaoStatus status);
    List<MedicaoEntity> findAllByOrcamentoId(Long orcamentoId);
}
