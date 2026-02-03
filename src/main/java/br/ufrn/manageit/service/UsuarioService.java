package br.ufrn.manageit.service;

import br.ufrn.manageit.domain.dto.CriarUsuarioDTO;
import br.ufrn.manageit.domain.model.Pessoa;
import br.ufrn.manageit.domain.model.Usuario;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.infra.exception.RegraNegocioException;
import br.ufrn.manageit.repository.PessoaRepository;
import br.ufrn.manageit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PessoaRepository pessoaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public Usuario criarUsuario(UUID pessoaId, String login, String senha) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pessoa não encontrada")
                );

        if (usuarioRepository.existsByLogin(login)) {
            throw new RegraNegocioException("Login já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setPessoa(pessoa);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );
    }

    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );
    }

    @Transactional
    public void alterarSenha(UUID usuarioId, String novaSenha) {
        Usuario usuario = buscarPorId(usuarioId);
        usuario.setSenha(novaSenha);
    }

    @Transactional
    public void deletarUsuario(UUID id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }




    //    REMOVER APOS TESTES

    public Usuario criarUsuario(CriarUsuarioDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(dto.pessoaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pessoa não encontrada"
                ));

        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(dto.senha());
        usuario.setUsuario(pessoa);

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
