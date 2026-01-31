package br.ufrn.manageit.controller;

import br.ufrn.manageit.domain.dto.AtualizarStatusItemDTO;
import br.ufrn.manageit.domain.enumeration.StatusItem;
import br.ufrn.manageit.domain.model.Item;
import br.ufrn.manageit.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/itens")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Item> cadastrarItem(@RequestBody @Valid Item item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(item));

    }

    @GetMapping
    public ResponseEntity<List<Item>> listarItems() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> listarItemPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.listarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Item> buscarItemPorPatrimonio(@RequestParam String patrimonio) {
        return ResponseEntity.ok(service.buscarPorPatrimonio(patrimonio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> atualizarItem(@PathVariable UUID id, @RequestBody @Valid Item item) {
        return ResponseEntity.ok(service.atualizarItem(id, item));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Item> atualizarStatusItem(@PathVariable UUID id, @RequestBody @Valid AtualizarStatusItemDTO status) {
        return ResponseEntity.ok(service.atualizarStatusItem(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
