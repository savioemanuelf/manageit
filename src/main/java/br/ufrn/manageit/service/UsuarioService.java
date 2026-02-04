package br.ufrn.manageit.service;

import br.ufrn.manageit.domain.dto.CriarUsuarioDTO;
import br.ufrn.manageit.domain.enumeration.Role;
import br.ufrn.manageit.domain.model.Pessoa;
import br.ufrn.manageit.domain.model.Usuario;
import br.ufrn.manageit.infra.exception.RecursoNaoEncontradoException;
import br.ufrn.manageit.infra.exception.RegraNegocioException;
import br.ufrn.manageit.repository.PessoaRepository;
import br.ufrn.manageit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name().replace("ROLE_", ""))
                .build();
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
        usuario.setSenha(passwordEncoder.encode(senha));
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
        usuario.setSenha(passwordEncoder.encode(novaSenha));
    }

    @Transactional
    public void deletarUsuario(UUID id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario criarUsuario(CriarUsuarioDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(dto.pessoaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pessoa não encontrada"
                ));

        if (usuarioRepository.existsByLogin(dto.login())) {
            throw new RegraNegocioException("Login já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setPessoa(pessoa);
        usuario.setRole(br.ufrn.manageit.domain.enumeration.Role.ROLE_USER);

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}