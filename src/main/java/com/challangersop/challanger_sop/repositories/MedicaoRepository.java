package com.challangersop.challanger_sop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.challangersop.challanger_sop.entities.MedicaoEntity;
import com.challangersop.challanger_sop.enums.MedicaoStatus;

import jakarta.persistence.LockModeType;

public interface MedicaoRepository extends JpaRepository<MedicaoEntity, Long>{
    Optional<MedicaoEntity> findByNumero(String numero);
    boolean existsByOrcamentoAndStatus(Long orcamentoId, MedicaoStatus status);
    List<MedicaoEntity> findAllByOrcamentoId(Long orcamentoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from MedicaoEntity m where m.id = :id")
    Optional<MedicaoEntity> findByIdForUpdate(Long id);
}
