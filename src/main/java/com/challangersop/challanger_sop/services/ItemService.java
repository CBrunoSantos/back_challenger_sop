package com.challangersop.challanger_sop.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.challangersop.challanger_sop.dtos.item.ItemCreateRequest;
import com.challangersop.challanger_sop.dtos.item.ItemResponse;
import com.challangersop.challanger_sop.dtos.item.ItemUpdateRequest;
import com.challangersop.challanger_sop.entities.ItemEntity;
import com.challangersop.challanger_sop.entities.OrcamentoEntity;
import com.challangersop.challanger_sop.enums.OrcamentoStatus;
import com.challangersop.challanger_sop.exceptions.BusinessRuleException;
import com.challangersop.challanger_sop.exceptions.NotFoundException;
import com.challangersop.challanger_sop.repositories.ItemRepository;
import com.challangersop.challanger_sop.repositories.OrcamentoRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrcamentoRepository orcamentoRepository;

    public ItemService(ItemRepository itemRepository, OrcamentoRepository orcamentoRepository) {
        this.itemRepository = itemRepository;
        this.orcamentoRepository = orcamentoRepository;
    }

    @Transactional
    public ItemResponse criar(ItemCreateRequest request) {
        OrcamentoEntity orcamento = orcamentoRepository.findById(request.getOrcamentoId())
            .orElseThrow(() -> new NotFoundException("Orçamento não encontrado."));

        if (orcamento.getStatus() == OrcamentoStatus.FINALIZADO) {
            throw new BusinessRuleException("Não é permitido incluir item em orçamento FINALIZADO.");
        }

        BigDecimal totalNovoItem = request.getQuantidade().multiply(request.getValorUnitario());
        BigDecimal somaAtual = itemRepository.sumValorItensByOrcamentoId(orcamento.getId());
        BigDecimal somaComNovo = somaAtual.add(totalNovoItem);

        if (somaComNovo.compareTo(orcamento.getValorTotal()) > 0) {
            throw new BusinessRuleException("A soma dos itens não pode ultrapassar o valor total do orçamento.");
        }

        ItemEntity entity = new ItemEntity();
        entity.setDescricao(request.getDescricao());
        entity.setQuantidade(request.getQuantidade());
        entity.setValorUnitario(request.getValorUnitario());
        entity.setQuantidadeAcumulada(BigDecimal.ZERO);
        entity.setOrcamento(orcamento);

        ItemEntity saved = itemRepository.save(entity);

        return ItemResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> listarPorOrcamento(Long orcamentoId) {
        List<ItemEntity> items = itemRepository.findAllByOrcamentoId(orcamentoId);
        return items.stream().map(ItemResponse::from).toList();
    }

    @Transactional
    public ItemResponse atualizar(Long itemId, ItemUpdateRequest request) {
        ItemEntity item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Item não encontrado."));

        OrcamentoEntity orcamento = item.getOrcamento();
        if (orcamento.getStatus() == OrcamentoStatus.FINALIZADO) {
            throw new BusinessRuleException("Não é permitido editar item de orçamento FINALIZADO.");
        }

        BigDecimal totalAtualItem = item.getQuantidade().multiply(item.getValorUnitario());
        BigDecimal somaAtual = itemRepository.sumValorItensByOrcamentoId(orcamento.getId());
        BigDecimal somaSemEsseItem = somaAtual.subtract(totalAtualItem);

        BigDecimal totalNovoItem = request.getQuantidade().multiply(request.getValorUnitario());
        BigDecimal somaComNovo = somaSemEsseItem.add(totalNovoItem);

        if (somaComNovo.compareTo(orcamento.getValorTotal()) > 0) {
            throw new BusinessRuleException("A soma dos itens não pode ultrapassar o valor total do orçamento.");
        }

        item.setDescricao(request.getDescricao());
        item.setQuantidade(request.getQuantidade());
        item.setValorUnitario(request.getValorUnitario());

        ItemEntity saved = itemRepository.save(item);
        return ItemResponse.from(saved);
    }

}
