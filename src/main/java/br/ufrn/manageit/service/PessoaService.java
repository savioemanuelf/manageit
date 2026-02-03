package br.ufrn.manageit.service;

import br.ufrn.manageit.domain.model.Pessoa;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PessoaService {

    private final PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Pessoa cadastrar(Pessoa pessoa) {
        return repository.save(pessoa);
    }

    public List<Pessoa> listarTodas() {
        return repository.findAll();
    }

    public Pessoa buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pessoa não encontrada")
                );
    }

    @Transactional
    public Pessoa atualizar(UUID id, Pessoa dados) {
        Pessoa pessoa = buscarPorId(id);

        pessoa.setNome(dados.getNome());
        pessoa.setCpf(dados.getCpf());
        pessoa.setEmail(dados.getEmail());
        pessoa.setTelefone(dados.getTelefone());

        return pessoa;
    }

    @Transactional
    public void deletar(UUID id) {
        Pessoa pessoa = buscarPorId(id);
        repository.delete(pessoa);
    }
}
