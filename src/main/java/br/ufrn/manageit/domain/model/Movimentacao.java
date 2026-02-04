package br.ufrn.manageit.domain.model;

import br.ufrn.manageit.domain.enumeration.StatusMovimentacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMovimentacao status;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Setor setorDestino;

    @ManyToOne(optional = false)
    private Usuario usuarioDestino;


    @ManyToMany
    @JoinTable(name = "movimentacao_itens",
               joinColumns = @JoinColumn(name = "movimentacao_id"),
               inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> itens = new HashSet<>();

    @Column(length = 500)
    private String observacao;

    public Movimentacao() {
        this.dataHora = LocalDateTime.now();
        this.status = StatusMovimentacao.PENDENTE;
    }

    public Movimentacao(Setor setorDestino,
                        Usuario usuarioDestino, Set<Item> itens,
                        String observacao) {
        this.status = StatusMovimentacao.PENDENTE;
        this.dataHora = LocalDateTime.now();
        this.setorDestino = setorDestino;
        this.usuarioDestino = usuarioDestino;
        this.itens = itens;
        this.observacao = observacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Set<Item> getItens() {
        return itens;
    }

    public void setItens(Set<Item> itens) {
        this.itens = itens;
    }

    public Usuario getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(Usuario usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public Setor getSetorDestino() {
        return setorDestino;
    }

    public void setSetorDestino(Setor setorDestino) {
        this.setorDestino = setorDestino;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusMovimentacao getStatus() {
        return status;
    }

    public void setStatus(StatusMovimentacao status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
