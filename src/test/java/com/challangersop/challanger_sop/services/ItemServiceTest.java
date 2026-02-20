package com.challangersop.challanger_sop.services;

import com.challangersop.challanger_sop.dtos.item.ItemCreateRequest;
import com.challangersop.challanger_sop.dtos.item.ItemUpdateRequest;
import com.challangersop.challanger_sop.entities.ItemEntity;
import com.challangersop.challanger_sop.entities.OrcamentoEntity;
import com.challangersop.challanger_sop.enums.OrcamentoStatus;
import com.challangersop.challanger_sop.exceptions.BusinessRuleException;
import com.challangersop.challanger_sop.exceptions.NotFoundException;
import com.challangersop.challanger_sop.repositories.ItemRepository;
import com.challangersop.challanger_sop.repositories.OrcamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void falharOrcamentoNaoExiste() {
        Long orcamentoId = 1L;

        ItemCreateRequest req = new ItemCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setDescricao("Item A");
        req.setQuantidade(new BigDecimal("10"));
        req.setValorUnitario(new BigDecimal("50"));

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.criar(req));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void falharOrcamentoFinalizado() {
        Long orcamentoId = 1L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.FINALIZADO);

        ItemCreateRequest req = new ItemCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setDescricao("Item A");
        req.setQuantidade(new BigDecimal("10"));
        req.setValorUnitario(new BigDecimal("50"));

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));

        assertThrows(BusinessRuleException.class, () -> itemService.criar(req));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void falharSomaUltrapassaValorTotalDoOrcamento() {
        Long orcamentoId = 1L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.ABERTO);
        orcamento.setValorTotal(new BigDecimal("1500.00"));

        ItemCreateRequest req = new ItemCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setDescricao("Item A");
        req.setQuantidade(new BigDecimal("10"));
        req.setValorUnitario(new BigDecimal("50"));

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));
        when(itemRepository.sumValorItensByOrcamentoId(orcamentoId)).thenReturn(new BigDecimal("1200.00"));

        assertThrows(BusinessRuleException.class, () -> itemService.criar(req));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void deveSalvarDentroDoLimiteDoOrcamento() {
        Long orcamentoId = 1L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.ABERTO);
        orcamento.setValorTotal(new BigDecimal("1500.00"));

        ItemCreateRequest req = new ItemCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setDescricao("Item A");
        req.setQuantidade(new BigDecimal("10"));
        req.setValorUnitario(new BigDecimal("50"));

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));
        when(itemRepository.sumValorItensByOrcamentoId(orcamentoId)).thenReturn(new BigDecimal("900.00"));

        ArgumentCaptor<ItemEntity> captor = ArgumentCaptor.forClass(ItemEntity.class);
        when(itemRepository.save(any(ItemEntity.class))).thenAnswer(invocation -> {
            ItemEntity e = invocation.getArgument(0);
            e.setId(10L);
            return e;
        });

        itemService.criar(req);

        verify(itemRepository, times(1)).save(captor.capture());
        ItemEntity salvo = captor.getValue();

        assertEquals("Item A", salvo.getDescricao());
        assertEquals(new BigDecimal("10"), salvo.getQuantidade());
        assertEquals(new BigDecimal("50"), salvo.getValorUnitario());
        assertEquals(BigDecimal.ZERO, salvo.getQuantidadeAcumulada());
        assertEquals(orcamento, salvo.getOrcamento());
    }

    @Test
    void atualizarFalharItemNaoExiste() {
        Long itemId = 10L;

        ItemUpdateRequest req = new ItemUpdateRequest();
        req.setDescricao("Novo nome");
        req.setQuantidade(new BigDecimal("5"));
        req.setValorUnitario(new BigDecimal("20"));

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.atualizar(itemId, req));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void atualizarFalharOrcamentoFinalizado() {
        Long itemId = 10L;
        Long orcamentoId = 1L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.FINALIZADO);

        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setOrcamento(orcamento);

        ItemUpdateRequest req = new ItemUpdateRequest();
        req.setDescricao("Novo nome");
        req.setQuantidade(new BigDecimal("5"));
        req.setValorUnitario(new BigDecimal("20"));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(BusinessRuleException.class, () -> itemService.atualizar(itemId, req));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void atualizarFalharSomaUltrapassaValorTotal() {
        Long itemId = 10L;
        Long orcamentoId = 1L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.ABERTO);
        orcamento.setValorTotal(new BigDecimal("1500.00"));

        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setOrcamento(orcamento);
        item.setQuantidade(new BigDecimal("1"));
        item.setValorUnitario(new BigDecimal("100"));

        ItemUpdateRequest req = new ItemUpdateRequest();
        req.setDescricao("Item atualizado");
        req.setQuantidade(new BigDecimal("10"));
        req.setValorUnitario(new BigDecimal("50"));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.sumValorItensByOrcamentoId(orcamentoId)).thenReturn(new BigDecimal("1200.00"));

        assertThrows(BusinessRuleException.class, () -> itemService.atualizar(itemId, req));
        verify(itemRepository, never()).save(any());
    }
}