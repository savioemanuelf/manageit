package br.ufrn.manageit.service;

import br.ufrn.manageit.domain.model.ProcessoSeletivo;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.repository.ProcessoSeletivoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcessoSeletivoService {
    private final ProcessoSeletivoRepository repository;

    public ProcessoSeletivoService(ProcessoSeletivoRepository processoSeletivoRepository) {
        this.repository = processoSeletivoRepository;
    }

    public ProcessoSeletivo buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Processo seletivo não encontrado para o id: " + id
                        )
                );
    }


    public List<ProcessoSeletivo> listarTodos() {
        return repository.findAll();
    }

    public ProcessoSeletivo salvar(ProcessoSeletivo processo) {
        return repository.save(processo);
    }

    public ProcessoSeletivo buscarPorNome(String nomeProcesso) {
        return repository.findByNomeProcesso(nomeProcesso)
                .orElseThrow(() ->   new RecursoNaoEncontradoException(
                        "Processo seletivo não encontrado para o nome: " + nomeProcesso
                )
            );
    }

    public void deletar(UUID id) {
        repository.deleteById(id);
    }

    public ProcessoSeletivo atualizar(UUID id, ProcessoSeletivo processo) {
        ProcessoSeletivo existente = buscarPorId(id);

        existente.setNomeProcesso(processo.getNomeProcesso());
        existente.setAnoProcesso(processo.getAnoProcesso());
        existente.setTipoProcesso(processo.getTipoProcesso());
        existente.setDataInicio(processo.getDataInicio());
        existente.setDataFim(processo.getDataFim());

        return repository.save(existente);
    }

}
