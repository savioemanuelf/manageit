package br.ufrn.manageit.controller;

import br.ufrn.manageit.domain.dto.CadastrarMovimentacaoDTO;
import br.ufrn.manageit.domain.enumeration.StatusMovimentacao;
import br.ufrn.manageit.domain.model.Movimentacao;
import br.ufrn.manageit.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {
    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Movimentacao> cadastrarMovimentacao(@RequestBody @Valid
                                                              CadastrarMovimentacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarMovimentacao(dto));

    }

    @GetMapping
    public ResponseEntity<List<Movimentacao>> listarMovimentacoes() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarMovimentacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> buscarMovimentacao(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarMovimentacaoPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Movimentacao>> listarMovimentacoesPorStatus(@PathVariable StatusMovimentacao status) {
        return ResponseEntity.ok(service.listarMovimentacoesPorStatus(status));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Movimentacao> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.cancelarMovimentacao(id));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Movimentacao> confirmar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.confirmarMovimentacao(id));
    }

    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<Movimentacao> devolucao(@PathVariable UUID id) {
        return ResponseEntity.ok(service.concluirMovimentacao(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMovimentacao(@PathVariable UUID id) {
        service.deletarMovimentacao(id);
        return ResponseEntity.noContent().build();
    }
}
