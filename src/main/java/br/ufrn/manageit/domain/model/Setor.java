package br.ufrn.manageit.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
public class Setor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O nome do setor é obrigatório")
    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

     @OneToOne
     @JoinColumn(name = "coordenador_id", unique = true)
     private Pessoa coordenador;

     @OneToOne
     @JoinColumn(name = "vice_coordenador_id", unique = true)
     private Pessoa viceCoordenador;

    public Setor(UUID id, String nome, String descricao, Pessoa coordenador, Pessoa viceCoordenador) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.coordenador = coordenador;
        this.viceCoordenador = viceCoordenador;
    }

    public Setor(String nome, String descricao, Pessoa coordenador, Pessoa viceCoordenador) {
        this.nome = nome;
        this.descricao = descricao;
        this.coordenador = coordenador;
        this.viceCoordenador = viceCoordenador;
    }

    public Setor() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pessoa getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Pessoa coordenador) {
        this.coordenador = coordenador;
    }

    public Pessoa getViceCoordenador() {
        return viceCoordenador;
    }

    public void setViceCoordenador(Pessoa viceCoordenador) {
        this.viceCoordenador = viceCoordenador;
    }
}
