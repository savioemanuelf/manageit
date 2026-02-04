package br.ufrn.manageit.config;

import br.ufrn.manageit.domain.enumeration.Role;
import br.ufrn.manageit.domain.model.Pessoa;
import br.ufrn.manageit.domain.model.Usuario;
import br.ufrn.manageit.repository.PessoaRepository;
import br.ufrn.manageit.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UsuarioRepository usuarioRepository,
                            PessoaRepository pessoaRepository,
                            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initAdmin() {
        // Verifica se já existe admin
        if (usuarioRepository.findByLogin("admin").isEmpty()) {
            // Cria pessoa para admin
            Pessoa pessoa = new Pessoa();
            pessoa.setNome("Administrador do Sistema");
            pessoa.setEmail("admin@ufrn.br");
            pessoa.setCpf("000.000.000-00");
            pessoa = pessoaRepository.save(pessoa);

            // Cria usuário admin
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin"));
            admin.setPessoa(pessoa);
            admin.setRole(Role.ROLE_ADMIN);

            usuarioRepository.save(admin);
            System.out.println("Usuário admin criado com sucesso!");
        }
    }
}