package com.challangersop.challanger_sop.controllers;

import com.challangersop.challanger_sop.dtos.medicao.MedicaoCreateRequest;
import com.challangersop.challanger_sop.dtos.medicao.MedicaoItemResponse;
import com.challangersop.challanger_sop.dtos.medicao.MedicaoItemUpsertRequest;
import com.challangersop.challanger_sop.dtos.medicao.MedicaoResponse;
import com.challangersop.challanger_sop.services.MedicaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicoes")
public class MedicaoController {

    private final MedicaoService medicaoService;

    public MedicaoController(MedicaoService medicaoService) {
        this.medicaoService = medicaoService;
    }

    @PostMapping
    public ResponseEntity<MedicaoResponse> criar(@Valid @RequestBody MedicaoCreateRequest request) {
        MedicaoResponse response = medicaoService.criar(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{medicaoId}/itens")
    public ResponseEntity<MedicaoItemResponse> upsertItem(@PathVariable Long medicaoId, @Valid @RequestBody MedicaoItemUpsertRequest request) {
        MedicaoItemResponse response = medicaoService.upsertItem(medicaoId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{medicaoId}/validar")
    public ResponseEntity<MedicaoResponse> validar(@PathVariable Long medicaoId) {
        MedicaoResponse response = medicaoService.validar(medicaoId);
        return ResponseEntity.ok(response);
    }
}
