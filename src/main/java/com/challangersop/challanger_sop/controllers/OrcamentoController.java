package com.challangersop.challanger_sop.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challangersop.challanger_sop.dtos.orcamento.OrcamentoCreateRequest;
import com.challangersop.challanger_sop.dtos.orcamento.OrcamentoResponse;
import com.challangersop.challanger_sop.dtos.orcamento.OrcamentoUpdateRequest;
import com.challangersop.challanger_sop.services.OrcamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {
    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoservice){
        this.orcamentoService = orcamentoservice;
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoCreateRequest request){
        OrcamentoResponse response = orcamentoService.criar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> buscar(@PathVariable Long id) {
        OrcamentoResponse response = orcamentoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody OrcamentoUpdateRequest request) {
        OrcamentoResponse response = orcamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<OrcamentoResponse> finalizar(@PathVariable Long id) {
        OrcamentoResponse response = orcamentoService.finalizar(id);
        return ResponseEntity.ok(response);
    }
}
