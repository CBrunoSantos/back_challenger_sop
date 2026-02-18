package com.challangersop.challanger_sop.controllers;

import com.challangersop.challanger_sop.dtos.item.ItemCreateRequest;
import com.challangersop.challanger_sop.dtos.item.ItemResponse;
import com.challangersop.challanger_sop.dtos.item.ItemUpdateRequest;
import com.challangersop.challanger_sop.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> criar(@Valid @RequestBody ItemCreateRequest request) {
        ItemResponse response = itemService.criar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orcamento/{orcamentoId}")
    public ResponseEntity<List<ItemResponse>> listarPorOrcamento(@PathVariable Long orcamentoId) {
        List<ItemResponse> response = itemService.listarPorOrcamento(orcamentoId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> atualizar(@PathVariable Long itemId, @Valid @RequestBody ItemUpdateRequest request) {
        ItemResponse response = itemService.atualizar(itemId, request);
        return ResponseEntity.ok(response);
    }
}
