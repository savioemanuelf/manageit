package br.ufrn.manageit.domain.model;

import br.ufrn.manageit.domain.enumeration.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public Usuario(UUID id, String login, String senha, Pessoa pessoa) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.pessoa = pessoa;
        this.role = Role.ROLE_USER;
    }

    public Usuario() {}

    public Pessoa getUsuario() {
        return pessoa;
    }

    public void setUsuario(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNome() {
        return getPessoa().getNome();
    }
}
