package br.ufrn.manageit.domain.model;

import br.ufrn.manageit.domain.dto.AtualizarStatusItemDTO;
import br.ufrn.manageit.domain.enumeration.StatusItem;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String patrimonio;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusItem status = StatusItem.DISPONIVEL;

    public Item(String patrimonio, String nome, String descricao, StatusItem status) {
        this.patrimonio = patrimonio;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }

    public Item(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
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

    public StatusItem getStatus() {
        return status;
    }

    public void setStatus(StatusItem status) {
        this.status = status;
    }

}
