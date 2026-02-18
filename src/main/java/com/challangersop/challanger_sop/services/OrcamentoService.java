package com.challangersop.challanger_sop.services;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.challangersop.challanger_sop.dtos.orcamento.OrcamentoCreateRequest;
import com.challangersop.challanger_sop.dtos.orcamento.OrcamentoResponse;
import com.challangersop.challanger_sop.dtos.orcamento.OrcamentoUpdateRequest;
import com.challangersop.challanger_sop.entities.OrcamentoEntity;
import com.challangersop.challanger_sop.enums.OrcamentoStatus;
import com.challangersop.challanger_sop.exceptions.BusinessRuleException;
import com.challangersop.challanger_sop.exceptions.NotFoundException;
import com.challangersop.challanger_sop.repositories.ItemRepository;
import com.challangersop.challanger_sop.repositories.OrcamentoRepository;

import jakarta.transaction.Transactional;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ItemRepository itemRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository, ItemRepository itemRepository){
        this.orcamentoRepository = orcamentoRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrcamentoResponse criar(OrcamentoCreateRequest request){
        boolean exists = orcamentoRepository.existsByNumeroProtocolo(request.getNumeroProtocolo());

        if(exists){
            throw new BusinessRuleException("Já existe um orçamento com este número de protocolo.");
        }

        OrcamentoEntity entity = new OrcamentoEntity();
        entity.setNumeroProtocolo(request.getNumeroProtocolo());
        entity.setTipo(request.getTipo());
        entity.setValorTotal(request.getValorTotal());
        entity.setDataCriacao(LocalDate.now());
        entity.setStatus(OrcamentoStatus.ABERTO);

        OrcamentoEntity saved;
        try {
            saved = orcamentoRepository.save(entity);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException("Não foi possível criar o orçamento. Verifique o número de protocolo.", ex);
        }

        return OrcamentoResponse.from(saved);
    }

    @Transactional
    public OrcamentoResponse atualizar(Long id, OrcamentoUpdateRequest request){
        OrcamentoEntity entity = orcamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Orçamento não encontrado."));

        if(entity.getStatus() == OrcamentoStatus.FINALIZADO){
            throw new BusinessRuleException("Não é permitido editar orçamento com status FINALIZADO.");
        }

        entity.setTipo(request.getTipo());
        entity.setValorTotal(request.getValorTotal());

        OrcamentoEntity saved = orcamentoRepository.save(entity);
        return OrcamentoResponse.from(saved);
    }

    @Transactional
    public OrcamentoResponse buscarPorId(Long id){
        OrcamentoEntity entity = orcamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Orçamento não encontrado"));

        return OrcamentoResponse.from(entity);
    }

    @Transactional
    public OrcamentoResponse finalizar(Long id){
        OrcamentoEntity entity = orcamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Orçamento não encontrado."));

        if (entity.getStatus() == OrcamentoStatus.FINALIZADO) {
            return OrcamentoResponse.from(entity);
        }

        BigDecimal somaItens = itemRepository.sumValorItensByOrcamentoId(entity.getId());
        if (somaItens == null) {
            somaItens = BigDecimal.ZERO;
        }

        if (somaItens.compareTo(entity.getValorTotal()) != 0) {
            throw new BusinessRuleException("Não é permitido finalizar: a soma dos itens deve ser igual ao valor total do orçamento.");
        }

        entity.setStatus(OrcamentoStatus.FINALIZADO);
        OrcamentoEntity saved = orcamentoRepository.save(entity);
        return OrcamentoResponse.from(saved);
    }
}
