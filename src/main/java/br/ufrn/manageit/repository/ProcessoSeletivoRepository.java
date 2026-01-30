package br.ufrn.manageit.repository;

import br.ufrn.manageit.domain.model.ProcessoSeletivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessoSeletivoRepository extends JpaRepository<ProcessoSeletivo, UUID> {
    Optional<List<ProcessoSeletivo>> findByNomeProcesso(String nomeProcesso);
    Optional<List<ProcessoSeletivo>> findByAnoProcesso(Integer anoProcesso);

}
