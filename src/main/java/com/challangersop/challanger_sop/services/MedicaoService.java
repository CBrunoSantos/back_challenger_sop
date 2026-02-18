package com.challangersop.challanger_sop.services;

import com.challangersop.challanger_sop.dtos.medicao.MedicaoCreateRequest;
import com.challangersop.challanger_sop.dtos.medicao.MedicaoItemResponse;
import com.challangersop.challanger_sop.dtos.medicao.MedicaoItemUpsertRequest;
import com.challangersop.challanger_sop.dtos.medicao.MedicaoResponse;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MedicaoService {

    private final MedicaoRepository medicaoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final ItemRepository itemRepository;
    private final ItemMedicaoRepository itemMedicaoRepository;

    public MedicaoService( MedicaoRepository medicaoRepository, OrcamentoRepository orcamentoRepository, ItemRepository itemRepository, ItemMedicaoRepository itemMedicaoRepository ) {
        this.medicaoRepository = medicaoRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.itemRepository = itemRepository;
        this.itemMedicaoRepository = itemMedicaoRepository;
    }

    @Transactional
    public MedicaoResponse criar(MedicaoCreateRequest request) {
        OrcamentoEntity orcamento = orcamentoRepository.findById(request.getOrcamentoId())
            .orElseThrow(() -> new NotFoundException("Orçamento não encontrado."));

        if (orcamento.getStatus() == OrcamentoStatus.FINALIZADO) {
            throw new BusinessRuleException("Não é permitido criar medição para orçamento FINALIZADO.");
        }

        MedicaoEntity medicao = new MedicaoEntity();
        medicao.setNumero(request.getNumero());
        medicao.setDataMedicao(request.getDataMedicao());
        medicao.setStatus(MedicaoStatus.ABERTA);
        medicao.setObservacao(request.getObservacao());
        medicao.setValorTotal(BigDecimal.ZERO);
        medicao.setOrcamento(orcamento);

        MedicaoEntity saved;
        try {
            saved = medicaoRepository.save(medicao);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException("Não foi possível criar a medição. Verifique número único e regra de medição aberta.", ex);
        }

        return MedicaoResponse.from(saved);
    }

    @Transactional
    public MedicaoItemResponse upsertItem(Long medicaoId, MedicaoItemUpsertRequest request) {
        MedicaoEntity medicao = medicaoRepository.findByIdForUpdate(medicaoId).orElseThrow(() -> new NotFoundException("Medição não encontrada."));

        if (medicao.getStatus() != MedicaoStatus.ABERTA) {
            throw new BusinessRuleException("Não é permitido alterar itens de uma medição VALIDADA.");
        }

        ItemEntity item = itemRepository.findByIdForUpdate(request.getItemId()).orElseThrow(() -> new NotFoundException("Item não encontrado."));

        Long orcamentoIdDaMedicao = medicao.getOrcamento().getId();
        Long orcamentoIdDoItem = item.getOrcamento().getId();

        if (!orcamentoIdDaMedicao.equals(orcamentoIdDoItem)) {
            throw new BusinessRuleException("O item informado não pertence ao orçamento desta medição.");
        }

        BigDecimal quantidadeAcumulada = item.getQuantidadeAcumulada();
        BigDecimal quantidadeTotalItem = item.getQuantidade();

        Optional<ItemMedicaoEntity> existenteOpt = itemMedicaoRepository.findByMedicaoIdAndItemId(medicaoId, item.getId());

        BigDecimal quantidadeAtualNaMedicao = BigDecimal.ZERO;
        if (existenteOpt.isPresent()) {
            quantidadeAtualNaMedicao = existenteOpt.get().getQuantidadeMedida();
        }

        BigDecimal acumuladaAjustada = quantidadeAcumulada.subtract(quantidadeAtualNaMedicao);

        BigDecimal novaAcumuladaConsiderada = acumuladaAjustada.add(request.getQuantidadeMedida());

        if (novaAcumuladaConsiderada.compareTo(quantidadeTotalItem) > 0) {
            throw new BusinessRuleException("Quantidade medida excede a quantidade total do item considerando a quantidade já acumulada.");
        }

        ItemMedicaoEntity registro;
        if (existenteOpt.isPresent()) {
            registro = existenteOpt.get();
        } else {
            registro = new ItemMedicaoEntity();
            registro.setMedicao(medicao);
            registro.setItem(item);
            registro.setValorUnitarioAplicado(item.getValorUnitario());
        }

        registro.setQuantidadeMedida(novaAcumuladaConsiderada);

        ItemMedicaoEntity saved = itemMedicaoRepository.save(registro);

        ItemMedicaoEntity reloaded = itemMedicaoRepository.findById(saved.getId())
            .orElseThrow(() -> new IllegalStateException("Item de medição salvo não foi encontrado."));

        return MedicaoItemResponse.from(reloaded);
    }

    @Transactional
    public MedicaoResponse validar(Long medicaoId) {
        MedicaoEntity medicao = medicaoRepository.findByIdForUpdate(medicaoId)
            .orElseThrow(() -> new NotFoundException("Medição não encontrada."));

        if (medicao.getStatus() == MedicaoStatus.VALIDADA) {
            return MedicaoResponse.from(medicao);
        }

        List<ItemMedicaoEntity> itensDaMedicao = itemMedicaoRepository.findAllByMedicaoId(medicaoId);
        if (itensDaMedicao.isEmpty()) {
            throw new BusinessRuleException("Não é permitido validar uma medição sem itens.");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (ItemMedicaoEntity im : itensDaMedicao) {
            ItemEntity item = itemRepository.findByIdForUpdate(im.getItem().getId()).orElseThrow(() -> new NotFoundException("Item não encontrado ao validar medição."));

            BigDecimal acumulada = item.getQuantidadeAcumulada();
            BigDecimal totalItem = item.getQuantidade();
            BigDecimal novaAcumulada = acumulada.add(im.getQuantidadeMedida());

            if (novaAcumulada.compareTo(totalItem) > 0) {
                throw new BusinessRuleException("A validação excede a quantidade total do item.");
            }

            item.setQuantidadeAcumulada(novaAcumulada);

            BigDecimal valorLinha = im.getQuantidadeMedida().multiply(im.getValorUnitarioAplicado());
            total = total.add(valorLinha);
        }

        medicao.setValorTotal(total);
        medicao.setStatus(MedicaoStatus.VALIDADA);

        MedicaoEntity saved = medicaoRepository.save(medicao);

        return MedicaoResponse.from(saved);
    }

}
