package br.ufrn.manageit.repository;

import br.ufrn.manageit.domain.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SetorRepository extends JpaRepository<Setor, UUID> {
    Optional<Setor> findByNome(String nome);
}
