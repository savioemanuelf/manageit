package br.ufrn.manageit.service;

import br.ufrn.manageit.domain.model.Setor;
import br.ufrn.manageit.infra.exception.BuscaInvalida;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.repository.SetorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SetorService {
    private final SetorRepository repository;

    public SetorService(SetorRepository repository) {
        this.repository = repository;
    }

    public Setor salvar(Setor setor) {
        return repository.save(setor);
    }

    public List<Setor> listarTodos() {
        return repository.findAll();
    }

    public Setor buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                new RecursoNaoEncontradoException("Setor não encontrado para o id: " + id)
        );
    }

    public Setor buscarPorNome(String nome) {
        if(nome == null) {
            throw new BuscaInvalida(
                    "Informe o nome do setor que deseja buscar"
            );
        }
        return repository.findByNome(nome)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Setor não encontrado com o nome: " + nome)
        );
    }

    public Setor atualizar(UUID id, Setor setor) {
        Setor setorExistente = buscarPorId(id);

        setorExistente.setNome(setor.getNome());
        setorExistente.setDescricao(setor.getDescricao());
        setorExistente.setCoordenador(setor.getCoordenador());
        setorExistente.setViceCoordenador(setor.getViceCoordenador());
        return repository.save(setorExistente);
    }

    public void remover(UUID id) {
        repository.deleteById(id);
    }
}
