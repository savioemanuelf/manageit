package br.ufrn.manageit.controller;

import br.ufrn.manageit.domain.model.Setor;
import br.ufrn.manageit.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setores")
public class SetorController {
    private final SetorService setorService;

    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @PostMapping
    public ResponseEntity<Setor> adicionarSetor(@RequestBody @Valid Setor setor) {
        Setor setorSalvo = setorService.salvar(setor);
        return ResponseEntity.status(HttpStatus.CREATED).body(setorSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Setor>> listarSetores() {
        return ResponseEntity.ok(setorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Setor> buscarSetorPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(setorService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Setor> buscarSetorPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(setorService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Setor> atualizarSetor(@PathVariable UUID id, @RequestBody @Valid Setor setor) {
        return ResponseEntity.ok(setorService.atualizar(id, setor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerSetor(@PathVariable UUID id) {
        setorService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
