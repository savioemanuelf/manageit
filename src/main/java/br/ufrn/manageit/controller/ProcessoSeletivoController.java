package br.ufrn.manageit.controller;

import br.ufrn.manageit.domain.model.ProcessoSeletivo;
import br.ufrn.manageit.service.ProcessoSeletivoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/processos-seletivos")
public class ProcessoSeletivoController {
    private final ProcessoSeletivoService service;

    public ProcessoSeletivoController(ProcessoSeletivoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProcessoSeletivo>> listarProcessos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoSeletivo> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProcessoSeletivo>> buscarPorNome(@RequestParam(required = false) String nomeProcesso,
                                                          @RequestParam(required = false) Integer ano) {
        return ResponseEntity.ok(service.buscar(nomeProcesso, ano));
    }

    @PostMapping
    public ResponseEntity<ProcessoSeletivo> criarProcesso(@RequestBody @Valid ProcessoSeletivo processo) {
        ProcessoSeletivo salvo = service.salvar(processo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcessoSeletivo> atulizarProcesso(@PathVariable UUID id,
                                                             @RequestBody @Valid ProcessoSeletivo processo) {
        return ResponseEntity.ok(service.atualizar(id, processo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProcesso(@PathVariable UUID id) {
        service.buscarPorId(id);
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
