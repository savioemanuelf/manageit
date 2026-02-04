package br.ufrn.manageit.controller;

import br.ufrn.manageit.domain.dto.AtualizarStatusItemDTO;
import br.ufrn.manageit.domain.enumeration.StatusItem;
import br.ufrn.manageit.domain.model.Item;
import br.ufrn.manageit.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/disponiveis")
    public ResponseEntity<List<Item>> listarItensDisponiveis() {
        List<Item> disponiveis = service.listarPorStatus(StatusItem.DISPONIVEL);
        return ResponseEntity.ok(disponiveis);
    }

    @GetMapping("/paginados")
    public ResponseEntity<Page<Item>> listarItensPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(direction), sort));

        return ResponseEntity.ok(service.listarPaginado(pageable));
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
