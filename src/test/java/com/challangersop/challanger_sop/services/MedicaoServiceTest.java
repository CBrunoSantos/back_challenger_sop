package com.challangersop.challanger_sop.services;

import com.challangersop.challanger_sop.dtos.medicao.MedicaoCreateRequest;
import com.challangersop.challanger_sop.dtos.medicao.MedicaoItemUpsertRequest;
import com.challangersop.challanger_sop.entities.ItemEntity;
import com.challangersop.challanger_sop.entities.ItemMedicaoEntity;
import com.challangersop.challanger_sop.entities.MedicaoEntity;
import com.challangersop.challanger_sop.entities.OrcamentoEntity;
import com.challangersop.challanger_sop.enums.MedicaoStatus;
import com.challangersop.challanger_sop.enums.OrcamentoStatus;
import com.challangersop.challanger_sop.exceptions.BusinessRuleException;
import com.challangersop.challanger_sop.exceptions.NotFoundException;
import com.challangersop.challanger_sop.repositories.ItemMedicaoRepository;
import com.challangersop.challanger_sop.repositories.ItemRepository;
import com.challangersop.challanger_sop.repositories.MedicaoRepository;
import com.challangersop.challanger_sop.repositories.OrcamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicaoServiceTest {

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @Mock
    private MedicaoRepository medicaoRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMedicaoRepository itemMedicaoRepository;

    @InjectMocks
    private MedicaoService medicaoService;

    @Test
    void criarMedicaoOrcamentoAberto() {
        Long orcamentoId = 10L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.ABERTO);

        MedicaoCreateRequest req = new MedicaoCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setNumero("M-0001");
        req.setDataMedicao(LocalDate.of(2026, 2, 20));
        req.setObservacao("Teste");

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));

        ArgumentCaptor<MedicaoEntity> captor = ArgumentCaptor.forClass(MedicaoEntity.class);
        when(medicaoRepository.save(any(MedicaoEntity.class))).thenAnswer(invocation -> {
            MedicaoEntity m = invocation.getArgument(0);
            m.setId(1L);
            return m;
        });

        medicaoService.criar(req);

        verify(medicaoRepository, times(1)).save(captor.capture());
        MedicaoEntity salvo = captor.getValue();

        assertEquals("M-0001", salvo.getNumero());
        assertEquals(LocalDate.of(2026, 2, 20), salvo.getDataMedicao());
        assertEquals(MedicaoStatus.ABERTA, salvo.getStatus());
        assertEquals(BigDecimal.ZERO, salvo.getValorTotal());
        assertEquals(orcamento, salvo.getOrcamento());
    }

    @Test
    void falharOrcamentoNaoExistir() {
        Long orcamentoId = 10L;

        MedicaoCreateRequest req = new MedicaoCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setNumero("M-0001");

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> medicaoService.criar(req));
        verify(medicaoRepository, never()).save(any());
    }

    @Test
    void falharOrcamentoFinalizado() {
        Long orcamentoId = 10L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.FINALIZADO);

        MedicaoCreateRequest req = new MedicaoCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setNumero("M-0001");

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));

        assertThrows(BusinessRuleException.class, () -> medicaoService.criar(req));
        verify(medicaoRepository, never()).save(any());
    }

    @Test
    void mapearDataIntegrity() {
        Long orcamentoId = 10L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setStatus(OrcamentoStatus.ABERTO);

        MedicaoCreateRequest req = new MedicaoCreateRequest();
        req.setOrcamentoId(orcamentoId);
        req.setNumero("M-0001");

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));
        when(medicaoRepository.save(any(MedicaoEntity.class))).thenThrow(new DataIntegrityViolationException("dup"));

        assertThrows(BusinessRuleException.class, () -> medicaoService.criar(req));
    }

    @Test
    void falharMedicaoNaoExiste() {
        Long medicaoId = 1L;

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.empty());

        MedicaoItemUpsertRequest req = new MedicaoItemUpsertRequest();
        req.setItemId(2L);
        req.setQuantidadeMedida(new BigDecimal("1"));

        assertThrows(NotFoundException.class, () -> medicaoService.upsertItem(medicaoId, req));
        verify(itemRepository, never()).findByIdForUpdate(anyLong());
        verify(itemMedicaoRepository, never()).save(any());
    }

    @Test
    void falharMedicaoNaoEstaAberta() {
        Long medicaoId = 1L;

        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setId(medicaoId);
        medicao.setStatus(MedicaoStatus.VALIDADA);

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.of(medicao));

        MedicaoItemUpsertRequest req = new MedicaoItemUpsertRequest();
        req.setItemId(2L);
        req.setQuantidadeMedida(new BigDecimal("1"));

        assertThrows(BusinessRuleException.class, () -> medicaoService.upsertItem(medicaoId, req));
        verify(itemRepository, never()).findByIdForUpdate(anyLong());
        verify(itemMedicaoRepository, never()).save(any());
    }

    @Test
    void falharItemNaoPertenceAoOrcamentoDaMedicao() {
        Long medicaoId = 1L;
        Long orcamentoMedicaoId = 10L;
        Long orcamentoItemId = 99L;

        OrcamentoEntity orcamentoMedicao = new OrcamentoEntity();
        orcamentoMedicao.setId(orcamentoMedicaoId);

        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setId(medicaoId);
        medicao.setStatus(MedicaoStatus.ABERTA);
        medicao.setOrcamento(orcamentoMedicao);

        OrcamentoEntity orcamentoItem = new OrcamentoEntity();
        orcamentoItem.setId(orcamentoItemId);

        ItemEntity item = new ItemEntity();
        item.setId(2L);
        item.setOrcamento(orcamentoItem);

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.of(medicao));
        when(itemRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(item));

        MedicaoItemUpsertRequest req = new MedicaoItemUpsertRequest();
        req.setItemId(2L);
        req.setQuantidadeMedida(new BigDecimal("1"));

        assertThrows(BusinessRuleException.class, () -> medicaoService.upsertItem(medicaoId, req));
        verify(itemMedicaoRepository, never()).save(any());
    }

    @Test
    void falharQuantidadeExcedeAcumulada() {
        Long medicaoId = 1L;
        Long orcamentoId = 10L;
        Long itemId = 2L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);

        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setId(medicaoId);
        medicao.setStatus(MedicaoStatus.ABERTA);
        medicao.setOrcamento(orcamento);

        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setOrcamento(orcamento);
        item.setQuantidade(new BigDecimal("10"));
        item.setQuantidadeAcumulada(new BigDecimal("9"));
        item.setValorUnitario(new BigDecimal("5"));

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.of(medicao));
        when(itemRepository.findByIdForUpdate(itemId)).thenReturn(Optional.of(item));
        when(itemMedicaoRepository.findByMedicaoIdAndItemId(medicaoId, itemId)).thenReturn(Optional.empty());

        MedicaoItemUpsertRequest req = new MedicaoItemUpsertRequest();
        req.setItemId(itemId);
        req.setQuantidadeMedida(new BigDecimal("2"));

        assertThrows(BusinessRuleException.class, () -> medicaoService.upsertItem(medicaoId, req));
        verify(itemMedicaoRepository, never()).save(any());
    }

    @Test
    void retornarResponseJaValidada() {
        Long medicaoId = 1L;

        OrcamentoEntity orc = new OrcamentoEntity();
        orc.setId(10L);


        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setId(medicaoId);
        medicao.setStatus(MedicaoStatus.VALIDADA);
        medicao.setOrcamento(orc);

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.of(medicao));

        medicaoService.validar(medicaoId);

        verify(itemMedicaoRepository, never()).findAllByMedicaoId(anyLong());
        verify(medicaoRepository, never()).save(any());
    }

    @Test
    void falharNaoTemItens() {
        Long medicaoId = 1L;

        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setId(medicaoId);
        medicao.setStatus(MedicaoStatus.ABERTA);

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.of(medicao));
        when(itemMedicaoRepository.findAllByMedicaoId(medicaoId)).thenReturn(List.of());

        assertThrows(BusinessRuleException.class, () -> medicaoService.validar(medicaoId));
        verify(medicaoRepository, never()).save(any());
    }

    @Test
    void falharValidacaoExcedeQuantidade() {
        Long medicaoId = 1L;
        Long itemId = 2L;

        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setId(medicaoId);
        medicao.setStatus(MedicaoStatus.ABERTA);

        ItemEntity itemRef = new ItemEntity();
        itemRef.setId(itemId);

        ItemMedicaoEntity im = new ItemMedicaoEntity();
        im.setId(100L);
        im.setItem(itemRef);
        im.setQuantidadeMedida(new BigDecimal("2"));
        im.setValorUnitarioAplicado(new BigDecimal("10"));

        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setQuantidade(new BigDecimal("10"));
        item.setQuantidadeAcumulada(new BigDecimal("9"));

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.of(medicao));
        when(itemMedicaoRepository.findAllByMedicaoId(medicaoId)).thenReturn(List.of(im));
        when(itemRepository.findByIdForUpdate(itemId)).thenReturn(Optional.of(item));

        assertThrows(BusinessRuleException.class, () -> medicaoService.validar(medicaoId));
        verify(medicaoRepository, never()).save(any());
    }

    @Test
    void validarCalcularTotal() {
        Long medicaoId = 1L;
        Long itemId = 2L;
        OrcamentoEntity orc = new OrcamentoEntity();
        orc.setId(10L);

        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setId(medicaoId);
        medicao.setStatus(MedicaoStatus.ABERTA);
        medicao.setOrcamento(orc);

        ItemEntity itemRef = new ItemEntity();
        itemRef.setId(itemId);

        ItemMedicaoEntity im = new ItemMedicaoEntity();
        im.setId(100L);
        im.setItem(itemRef);
        im.setQuantidadeMedida(new BigDecimal("2"));
        im.setValorUnitarioAplicado(new BigDecimal("10"));

        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setQuantidade(new BigDecimal("10"));
        item.setQuantidadeAcumulada(new BigDecimal("0"));

        when(medicaoRepository.findByIdForUpdate(medicaoId)).thenReturn(Optional.of(medicao));
        when(itemMedicaoRepository.findAllByMedicaoId(medicaoId)).thenReturn(List.of(im));
        when(itemRepository.findByIdForUpdate(itemId)).thenReturn(Optional.of(item));

        ArgumentCaptor<MedicaoEntity> captor = ArgumentCaptor.forClass(MedicaoEntity.class);
        when(medicaoRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        medicaoService.validar(medicaoId);

        MedicaoEntity salvo = captor.getValue();
        assertEquals(MedicaoStatus.VALIDADA, salvo.getStatus());
        assertEquals(new BigDecimal("20"), salvo.getValorTotal());
    }

    @Test
    void listarOrcamentoFalharOrcamentoNaoExiste() {
        Long orcamentoId = 10L;

        when(orcamentoRepository.existsById(orcamentoId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> medicaoService.listarPorOrcamento(orcamentoId));
        verify(medicaoRepository, never()).findByOrcamentoIdOrderByDataMedicaoDescIdDesc(anyLong());
    }

    @Test
    void listarItensDaMedicaoFalharMedicaoNaoExiste() {
        Long medicaoId = 1L;

        when(medicaoRepository.existsById(medicaoId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> medicaoService.listarItensDaMedicao(medicaoId));
        verify(itemMedicaoRepository, never()).findByMedicaoIdOrderByIdAsc(anyLong());
    }
}