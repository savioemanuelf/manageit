package br.ufrn.manageit.repository;

import br.ufrn.manageit.domain.enumeration.StatusMovimentacao;
import br.ufrn.manageit.domain.model.Movimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, UUID> {
    List<Movimentacao> findAllByStatus(StatusMovimentacao status);
    Page<Movimentacao> findBySetorDestinoId(UUID setorId, Pageable pageable);
    Page<Movimentacao> findByUsuarioDestinoId(UUID usuarioId, Pageable pageable);
}
