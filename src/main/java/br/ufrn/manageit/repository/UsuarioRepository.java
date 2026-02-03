package br.ufrn.manageit.repository;

import br.ufrn.manageit.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByLogin(String login);

    boolean existsByLogin(String login);
}
