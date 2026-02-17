package com.challangersop.challanger_sop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challangersop.challanger_sop.entities.OrcamentoEntity;

public interface OrcamentoRepository extends JpaRepository<OrcamentoEntity, Long> {
    Optional<OrcamentoEntity> findByNumeroProtocolo(String numeroProtocolo);
    boolean existsByNumeroProtocolo(String numeroProtocolo);
}
