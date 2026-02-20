package com.challangersop.challanger_sop.services;

import com.challangersop.challanger_sop.entities.OrcamentoEntity;
import com.challangersop.challanger_sop.enums.OrcamentoStatus;
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
class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrcamentoService orcamentoService;

    @Test
    void finalizarSomaItensIgualValorTotal() {
        Long orcamentoId = 1L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setValorTotal(new BigDecimal("1500.00"));
        orcamento.setStatus(OrcamentoStatus.ABERTO);

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));
        when(itemRepository.sumValorItensByOrcamentoId(orcamentoId)).thenReturn(new BigDecimal("1500.00"));

        ArgumentCaptor<OrcamentoEntity> captor = ArgumentCaptor.forClass(OrcamentoEntity.class);
        when(orcamentoRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        orcamentoService.finalizar(orcamentoId);

        OrcamentoEntity salvo = captor.getValue();
        assertEquals(OrcamentoStatus.FINALIZADO, salvo.getStatus());
        verify(orcamentoRepository, times(1)).save(any(OrcamentoEntity.class));
    }

    @Test
    void naoFinalizarSomaItensDiferenteValorTotal() {
        Long orcamentoId = 1L;

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setId(orcamentoId);
        orcamento.setValorTotal(new BigDecimal("1500.00"));
        orcamento.setStatus(OrcamentoStatus.ABERTO);

        when(orcamentoRepository.findById(orcamentoId)).thenReturn(Optional.of(orcamento));
        when(itemRepository.sumValorItensByOrcamentoId(orcamentoId)).thenReturn(new BigDecimal("1200.00"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orcamentoService.finalizar(orcamentoId));

        assertNotNull(ex.getMessage());
        verify(orcamentoRepository, never()).save(any(OrcamentoEntity.class));
    }
}