package br.ufrn.manageit.repository;

import br.ufrn.manageit.domain.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {
    Page<Pessoa> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Optional<Pessoa> findByCpf(String cpf);
}
